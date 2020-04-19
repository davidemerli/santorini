package it.polimi.ingsw.psp1.santorini.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int socketPort;

    public Server(int socketPort) {
        this.socketPort = socketPort;
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
