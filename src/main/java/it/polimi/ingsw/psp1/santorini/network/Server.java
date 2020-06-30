package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.controller.Controller;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerConnectedToGame;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerKeepAlive;
import it.polimi.ingsw.psp1.santorini.view.RemoteView;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Manages the game and the progress of the turns of the various clients
 */
public class Server implements Runnable {

    private final int GAME_ID_DIGITS = 4;

    private final Random rnd;

    private final int socketPort;

    private final List<ClientConnectionHandler> clientsToRelocate;

    private final Map<ClientConnectionHandler, Player> twoPlayerGameQueue;
    private final Map<ClientConnectionHandler, Player> threePlayerGameQueue;

    private final Map<Game, Map<ClientConnectionHandler, Player>> games;

    private final ExecutorService pool;

    /**
     * Generic constructor using socket port
     * Creates queues for the players
     * Creates list with all clients who needs to be relocate
     * Creates a thread pool and starts it
     *
     * @param socketPort used port
     */
    public Server(int socketPort) {
        this.socketPort = socketPort;
        this.clientsToRelocate = Collections.synchronizedList(new ArrayList<>());
        this.twoPlayerGameQueue = new ConcurrentHashMap<>();
        this.threePlayerGameQueue = new ConcurrentHashMap<>();

        this.games = Collections.synchronizedMap(new LinkedHashMap<>());

        this.pool = Executors.newFixedThreadPool(128);

        ScheduledExecutorService gameStarterPool = Executors.newSingleThreadScheduledExecutor();

        gameStarterPool.scheduleAtFixedRate(this::gameStarter,
                0, //delay
                500, TimeUnit.MILLISECONDS);//run every 500ms

        //TODO: terminate gameStarterPool

        this.rnd = new Random();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Accepts new client connecting to the server and puts them in the relocation list
     * When a client has its player name set, it will be moved in the waitingForGame list
     */
    @Override
    public void run() {
        System.out.println("Server ready to receive clients.");

        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                client.setSoTimeout(20000);

                System.out.println("Accepted client: " + client.getInetAddress());
                //when a client connects it is put into a list of connected sockets and a ClientHandler is created
                ClientConnectionHandler clientHandler = new ClientConnectionHandler(this, client);
                //sending a first keep alive packet so the client can send another backwards
                clientHandler.sendPacket(new ServerKeepAlive());

                pool.execute(clientHandler);

                clientsToRelocate.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the new game has been filled, it starts
     */
    private void gameStarter() {
        try {
            synchronized (games) {
                games.entrySet().stream()
                        .filter(e -> e.getKey().hasEnded() || e.getValue().isEmpty())
                        .peek(e -> e.getValue().keySet().forEach(ClientConnectionHandler::closeConnection))
                        .map(Map.Entry::getKey)
                        .forEach(games::remove);

                games.keySet().stream()
                        .filter(g -> !g.hasStarted())
                        .forEach(game -> {
                    Map<ClientConnectionHandler, Player> gamePlayers = games.get(game);

                    boolean full = fillGame(game);

                    if (full) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if the game has been filled with the players (two or three)
     *
     * @param game New created game
     * @return true if game contains all players needed (two or three)
     */
    private boolean fillGame(Game game) {
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
                    //TODO: recheck
                    if (gamePlayers.get(ch).equals(waitingClient.get().getValue())) {
                        gamePlayers.values().forEach(p -> ch.sendPacket(new ServerConnectedToGame(
                                p.getName(), game.getGameID(), game.getPlayerNumber())));
                    } else {
                        ch.sendPacket(new ServerConnectedToGame(
                                waitingClient.get().getValue().getName(), game.getGameID(), game.getPlayerNumber()));
                    }
                }
            }
        }

        return gamePlayers.size() == game.getPlayerNumber();
    }

    /**
     * Creates a new game instance with the player that created it as the first player
     *
     * @param connectionHandler with the player that created the game
     * @param playerNumber number of players
     * @throws UnsupportedOperationException if connection between player and server is already assigned
     * @throws IllegalStateException if players has not set a name yet
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

        synchronized (games) {
            //create a new game
            Game newGame = new Game(generateGameID(), playerNumber);

            //add a new game to the list of games
            games.put(newGame, new LinkedHashMap<>(Map.of(connectionHandler, optPlayer.get())));

            connectionHandler.sendPacket(new ServerConnectedToGame(optPlayer.get().getName(),
                    newGame.getGameID(), playerNumber));
        }
    }

    /**
     * Disconnects player from the game
     *
     * @param connectionHandler with the players that joins the game
     */
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

    /**
     * Connects players with the game and they joins it
     *
     * @param connectionHandler with the available players
     * @param gameRoom room's name
     * @throws IllegalArgumentException if room's name does not exists
     * @throws IllegalStateException if game is already full
     * @throws IllegalStateException if players has not set a name yet
     */
    public void joinGame(ClientConnectionHandler connectionHandler, String gameRoom) {
        Optional<Game> toJoin = games.keySet().stream()
                .filter(game -> game.getGameID().equals(gameRoom))
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
                game.values().forEach(pp -> connectionHandler.sendPacket(new ServerConnectedToGame(
                        pp.getName(), toJoin.get().getGameID(), toJoin.get().getPlayerNumber())));
            } else {
                connectionHandler.sendPacket(new ServerConnectedToGame(p.getName(),
                        toJoin.get().getGameID(), toJoin.get().getPlayerNumber()));
            }
        }

        clientsToRelocate.remove(connectionHandler);
    }

    /**
     * Puts available players in a queue, used to insert players into the game
     *
     * @param connectionHandler with the available players
     * @param playerNumber number of players in a game
     * @throws IllegalStateException if players has not set a name yet
     * @throws IllegalArgumentException if number of players is not two or three
     */
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

    /**
     * Checks the uniqueness of the username
     *
     * @param username player's name
     * @return true if username is unique
     */
    public boolean isUsernameValid(String username) {
        return username.trim().matches("(.)+");
    }

    /**
     * Checks if an username is unique
     *
     * @param username to check
     * @return true if the username is unique
     */
    public boolean isUsernameUnique(String username) {
        Set<String> assignedPlayerUsernames = games.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(Player::getName)
                .collect(Collectors.toSet());

        Stream.concat(twoPlayerGameQueue.values().stream(),
                threePlayerGameQueue.values().stream())
                .map(Player::getName).forEach(assignedPlayerUsernames::add);

        clientsToRelocate.stream()
                .filter(cch -> cch.getPlayer().isPresent())
                .forEach(cch -> assignedPlayerUsernames.add(cch.getPlayer().get().getName()));

        return !assignedPlayerUsernames.contains(username);
    }

    /**
     * Generates an unique game ID
     *
     * @return a new unique game ID
     */
    private String generateGameID() {
        Set<String> assignedGameIDs = games.keySet().stream()
                .map(Game::getGameID)
                .collect(Collectors.toSet());

        String gameID;
        StringBuilder sb = new StringBuilder();

        do {
            IntStream.range(0, GAME_ID_DIGITS).forEach(i -> sb.append(rnd.nextInt(10)));
            gameID = sb.toString();
            sb.setLength(0);
        } while (assignedGameIDs.contains(gameID));

        return gameID;
    }
}