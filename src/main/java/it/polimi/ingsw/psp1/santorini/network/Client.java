package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private ObjectOutputStream objectOutputStream;
    private ServerHandler serverHandler;
    private Socket server;

    private String ip;
    private int port;
    private boolean connected;

    public void connectToServer(String ip, int port) {
        this.ip = ip;
        this.port = port;

        new Thread(this).start();
    }

    public void disconnect() {
        try {
            connected = false;

            if (server != null) {
                server.close();
                server = null;
            }
        } catch (IOException e) {
            System.out.println("Server connection closed wrongly");
        }
    }

    public void sendPacket(Packet<ClientHandler> packet) {
        try {
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            PrintUtils.printFromCommand(Color.RED + "Connection to server has crashed, please reconnect",
                    0, -1, true);

            disconnect();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try (Socket serverSocket = new Socket(ip, port)) {
            connected = true;
            server = serverSocket;

            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream = new ObjectOutputStream(server.getOutputStream());

            while (connected) {
                if (serverHandler != null) {
                    Object object = objectInputStream.readObject();
                    ((Packet<ServerHandler>) object).processPacket(serverHandler);
                }
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            PrintUtils.printFromCommand(Color.RED + "Connection to server has crashed, please reconnect",
                    0, -1, true);

            disconnect();
        }
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public boolean isConnected() {
        return connected;
    }
}
