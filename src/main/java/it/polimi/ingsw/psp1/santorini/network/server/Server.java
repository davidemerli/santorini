package it.polimi.ingsw.psp1.santorini.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private final int socketPort;

    private final List<Socket> lobby;

    public Server(int socketPort) {
        this.socketPort = socketPort;
        this.lobby = new ArrayList<>();
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(socketPort)) {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                System.out.println("Accepted client: " + client.getInetAddress());


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
