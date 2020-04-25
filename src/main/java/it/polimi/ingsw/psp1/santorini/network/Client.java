package it.polimi.ingsw.psp1.santorini.network;

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
    private boolean running;

    public Client() {
        this.running = true;
    }

    public void connectToServer(String ip, int port) {
        this.ip = ip;
        this.port = port;

        new Thread(this).start();
    }

    public void disconnect() {
//        try {
//            server.close();
//            server = null;
//        } catch (IOException e) {
//            System.out.println("Server connection closed wrongly");
//        }
    }

    public synchronized void close() {
        this.running = false;
    }

    public void sendPacket(Packet<ClientHandler> packet) {
        try {
            if (objectOutputStream == null) {
                objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            }

            objectOutputStream.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try (Socket server = new Socket(ip, port)) {
            this.server = server;

            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());

            while (true) {
                if (serverHandler != null) {
                    Object object = objectInputStream.readObject();
                    ((Packet<ServerHandler>) object).processPacket(serverHandler);
                }
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
}
