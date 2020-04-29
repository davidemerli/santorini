package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.controller.Controller;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
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

    private final int socketPort;

    private final List<ClientConnectionHandler> clientsToRelocate;

    private final Map<ClientConnectionHandler, Player> waitingForGame;
    private final Map<Game, Map<ClientConnectionHandler, Player>> games;

    private final ExecutorService pool;

    public Server(int socketPort) {
        this.socketPort = socketPort;
        this.clientsToRelocate = Collections.synchronizedList(new ArrayList<>());
        this.waitingForGame = new ConcurrentHashMap<>();
        this.games = Collections.synchronizedMap(new LinkedHashMap<>());

        this.pool = Executors.newFixedThreadPool(128);

        this.pool.execute(this::gameStarter);
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

            for (Game game : games.keySet()) {
                Map<ClientConnectionHandler, Player> gamePlayers = games.get(game);

                if (gamePlayers.size() < game.getPlayerNumber()) {
                    Optional<Map.Entry<ClientConnectionHandler, Player>> waitingClient =
                            waitingForGame.entrySet().stream().findFirst();

                    if (waitingClient.isPresent()) {
                        gamePlayers.put(waitingClient.get().getKey(), waitingClient.get().getValue());

                        waitingForGame.remove(waitingClient.get().getKey());
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

                        pool.submit(game);
                    }
                }
            }
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

        if (!waitingForGame.containsKey(connectionHandler)) {
            throw new UnsupportedOperationException("Username not set");
        }

        //create a new game
        Game newGame = new Game(playerNumber);

        //create the list of the ClientHandlers of the players that will join the current game
        Map<ClientConnectionHandler, Player> clients = new LinkedHashMap<>();
        clients.put(connectionHandler, waitingForGame.get(connectionHandler));
        //add a new game to the list of games
        games.put(newGame, clients);

        waitingForGame.remove(connectionHandler);
    }

    /**
     * Adds a player and its ClientHandler to the waitingList
     *
     * @param player            instance with the name already set (power is not set yet)
     * @param connectionHandler player ClientHandler
     */
    public void addToWait(Player player, ClientConnectionHandler connectionHandler) {
        if (!clientsToRelocate.contains(connectionHandler)) {
            throw new UnsupportedOperationException("Given client handler is not from a player that needs relocation");
        }

        boolean sameName = waitingForGame.values().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()));

        if (sameName) {
            throw new UnsupportedOperationException("There is already a player with the same name");
        }

        waitingForGame.put(connectionHandler, player);

        clientsToRelocate.remove(connectionHandler);
    }

    public void disconnectClient(ClientConnectionHandler connectionHandler) {
        waitingForGame.remove(connectionHandler);
        clientsToRelocate.remove(connectionHandler);

        Optional<Game> optGame = games.entrySet().stream()
                .filter(e -> e.getValue().containsKey(connectionHandler))
                .map(Map.Entry::getKey).findFirst();

        if (optGame.isPresent()) {
            if (!optGame.get().hasStarted()) {
                games.get(optGame.get()).remove(connectionHandler);
            }
        }
    }
}
