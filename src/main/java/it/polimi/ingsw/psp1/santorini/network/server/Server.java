package it.polimi.ingsw.psp1.santorini.network.server;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {

    private final int socketPort;

    private final List<ClientHandler> clientsToRelocate;
    private final Map<ClientHandler, Player> waitingForGame;

    private Map<Game, List<ClientHandler>> games;

    public Server(int socketPort) {
        this.socketPort = socketPort;
        this.clientsToRelocate = new ArrayList<>();
        this.waitingForGame = new LinkedHashMap<>();
        this.games = new HashMap<>();
    }

    /**
     * Accepts new client connecting to the server and puts them in the relocation list
     * When a client has its player name set, it will be moved in the waitingForGame list
     */
    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(socketPort)) {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                System.out.println("Accepted client: " + client.getInetAddress());
                //when a client connects it is put into a list of connected sockets and a ClientHandler is created
                clientsToRelocate.add(new ClientConnectionHandler(this, client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new game instance with the player that created it as the first player
     *
     * @param connectionHandler with the player that created the game
     */
    public void createGame(ClientConnectionHandler connectionHandler) {
        if(!waitingForGame.containsKey(connectionHandler)) {
            throw new UnsupportedOperationException("Connection already assigned to game");
        }

        //create a new game and add the player that instantiated it
        Game newGame = new Game();
        newGame.addPlayer(waitingForGame.get(connectionHandler));
        //create the list of the ClientHandlers of the players that will join the current game
        List<ClientHandler> clients = new ArrayList<>();
        clients.add(connectionHandler);
        //add a new game to the list of games
        games.put(newGame, clients);

        waitingForGame.remove(connectionHandler);
    }

    /**
     * Adds a player and its ClientHandler to the waitingList
     *
     * @param player instance with the name already set (power is not set yet)
     * @param connectionHandler player ClientHandler
     */
    public void addToWait(Player player, ClientConnectionHandler connectionHandler) {
        if(!clientsToRelocate.contains(connectionHandler)) {
            throw new UnsupportedOperationException("Given client handler is not from a player that needs relocation");
        }

        boolean sameName = waitingForGame.values().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()));

        if(sameName) {
            throw new UnsupportedOperationException("There is already a player with the same name");
        }

        waitingForGame.put(connectionHandler, player);
    }
}
