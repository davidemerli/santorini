package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.controller.Controller;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerConnectedToGame;
import it.polimi.ingsw.psp1.santorini.view.RemoteView;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server implements Runnable {

    private final Random rnd;

    private final int socketPort;

    private final List<ClientConnectionHandler> clientsToRelocate;

    private final Map<ClientConnectionHandler, Player> twoPlayerGameQueue;
    private final Map<ClientConnectionHandler, Player> threePlayerGameQueue;

    private final Map<Game, Map<ClientConnectionHandler, Player>> games;

    private final ExecutorService pool;

    public Server(int socketPort) {
        this.socketPort = socketPort;
        this.clientsToRelocate = Collections.synchronizedList(new ArrayList<>());
        this.twoPlayerGameQueue = new ConcurrentHashMap<>();
        this.threePlayerGameQueue = new ConcurrentHashMap<>();

        this.games = new ConcurrentHashMap<>();

        this.pool = Executors.newFixedThreadPool(128);

        this.pool.execute(this::gameStarter);

        this.rnd = new Random();
    }

    /**
     * Accepts new client connecting to the server and puts them in the relocation list
     * When a client has its player name set, it will be moved in the waitingForGame list
     */
    @Override
    public void run() {
        System.out.println("Server ready to receive clients.");

        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
//                client.setSoTimeout(20000);

                System.out.println("Accepted client: " + client.getInetAddress());
                //when a client connects it is put into a list of connected sockets and a ClientHandler is created
                ClientConnectionHandler clientHandler = new ClientConnectionHandler(this, client);
                pool.execute(clientHandler);

                clientsToRelocate.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gameStarter() {
        System.out.println("Game starter thread ready");

        while (true) {//TODO: make server stoppable
            //Removes terminated games from the games map
            games.entrySet().stream()
                    .filter(e -> e.getKey().hasEnded())
                    .peek(e -> e.getValue().keySet().forEach(ClientConnectionHandler::closeConnection))
                    .map(Map.Entry::getKey)
                    .forEach(games::remove);

            games.keySet().stream().filter(g -> !g.hasStarted()).forEach(game -> {
                Map<ClientConnectionHandler, Player> gamePlayers = games.get(game);

                if (gamePlayers.size() < game.getPlayerNumber()) {
                    Optional<Map.Entry<ClientConnectionHandler, Player>> waitingClient;

                    if (game.getPlayerNumber() == 2) {
                        waitingClient = twoPlayerGameQueue.entrySet().stream().findFirst();
                    } else {
                        waitingClient = threePlayerGameQueue.entrySet().stream().findFirst();
                    }

                    if (waitingClient.isPresent()) {
                        gamePlayers.put(waitingClient.get().getKey(), waitingClient.get().getValue());

                        if (game.getPlayerNumber() == 2) {
                            twoPlayerGameQueue.remove(waitingClient.get().getKey());
                        } else {
                            threePlayerGameQueue.remove(waitingClient.get().getKey());
                        }

                        for (ClientConnectionHandler ch : gamePlayers.keySet()) {
                            if (gamePlayers.get(ch).equals(waitingClient.get().getValue())) {
                                gamePlayers.values().forEach(p -> ch.sendPacket(new ServerConnectedToGame(p.getName())));
                            } else {
                                ch.sendPacket(new ServerConnectedToGame(waitingClient.get().getValue().getName()));
                            }
                        }
                    }
                }

                if (gamePlayers.size() == game.getPlayerNumber()) {
                    Controller controller = new Controller(game);
                    List<View> views = new ArrayList<>();
                    gamePlayers.forEach((cch, p) -> views.add(new RemoteView(p, cch)));

                    //views observe model
                    views.forEach(game::addObserver);

                    //controller observes views
                    views.forEach(v -> v.addObserver(controller));

                    //add players to the game
                    views.forEach(v -> game.addPlayer(v.getPlayer()));

                    game.startGame();
                }
            });
        }
    }

    /**
     * Creates a new game instance with the player that created it as the first player
     *
     * @param connectionHandler with the player that created the game
     */
    public void createGame(ClientConnectionHandler connectionHandler, int playerNumber) {
        boolean isInGame = games.values().stream()
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .anyMatch(connectionHandler::equals);

        if (isInGame) {
            throw new UnsupportedOperationException("Connection already assigned to game");
        }

        Optional<Player> optPlayer = connectionHandler.getPlayer();

        if (optPlayer.isEmpty()) {
            throw new IllegalStateException("You need to set a name first");
        }

        //create a new game
        Game newGame = new Game(generateValidID(), playerNumber);

        //add a new game to the list of games
        games.put(newGame, new LinkedHashMap<>(Map.of(connectionHandler, optPlayer.get())));

        connectionHandler.sendPacket(new ServerConnectedToGame(optPlayer.get().getName()));
    }

    public void disconnectClient(ClientConnectionHandler connectionHandler) {
        twoPlayerGameQueue.remove(connectionHandler);
        threePlayerGameQueue.remove(connectionHandler);
        clientsToRelocate.remove(connectionHandler);

        Optional<Game> optGame = games.entrySet().stream()
                .filter(e -> e.getValue().containsKey(connectionHandler))
                .map(Map.Entry::getKey).findFirst();

        if (optGame.isPresent() && !optGame.get().hasStarted()) {
            games.get(optGame.get()).remove(connectionHandler);
        }
    }

    public void joinGame(ClientConnectionHandler connectionHandler, int gameRoom) {
        Optional<Game> toJoin = games.keySet().stream()
                .filter(game -> game.getGameID() == gameRoom)
                .findFirst();

        if (toJoin.isEmpty()) {
            throw new IllegalArgumentException("No game found with given ID");
        }

        if (games.get(toJoin.get()).size() == toJoin.get().getPlayerNumber()) {
            throw new IllegalStateException("Game is already full");
        }

        Optional<Player> optPlayer = connectionHandler.getPlayer();

        if (optPlayer.isEmpty()) {
            throw new IllegalStateException("You need to set a name first");
        }

        Map<ClientConnectionHandler, Player> game = games.get(toJoin.get());
        game.put(connectionHandler, optPlayer.get());
        for (Player p : game.values()) {
            if (p.equals(connectionHandler.getPlayer().orElse(null))) {
                game.values().forEach(pp -> connectionHandler.sendPacket(new ServerConnectedToGame(pp.getName())));
            } else {
                connectionHandler.sendPacket(new ServerConnectedToGame(p.getName()));
            }
        }
        clientsToRelocate.remove(connectionHandler);
    }

    public void joinQueue(ClientConnectionHandler connectionHandler, int playerNumber) {
        Optional<Player> optPlayer = connectionHandler.getPlayer();

        if (optPlayer.isEmpty()) {
            throw new IllegalStateException("You need to set a name first");
        }

        if (playerNumber == 2) {
            twoPlayerGameQueue.put(connectionHandler, optPlayer.get());
        } else if (playerNumber == 3) {
            threePlayerGameQueue.put(connectionHandler, optPlayer.get());
        } else {
            throw new IllegalArgumentException("Invalid player number");
        }

        clientsToRelocate.remove(connectionHandler);
    }

    private int generateValidID() {
        Set<Integer> assignedIDs = games.keySet().stream()
                .map(Game::getGameID)
                .collect(Collectors.toSet());

        int gameID;

        do {//generates a random number between 1000 and 9999
            gameID = 1000 + rnd.nextInt(9000);
        } while (assignedIDs.contains(gameID));

        return gameID;
    }
}