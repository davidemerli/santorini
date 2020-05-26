package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private final Object lock = new Object();

    private ObjectOutputStream objectOutputStream;
    private ServerHandler serverHandler;
    private Socket server;

    private String ip;
    private int port;
    private boolean connected;

    public void connectToServer(String ip, int port) {
        disconnect();

        this.ip = ip;
        this.port = port;

        try {
            synchronized (lock) {
                server = new Socket(ip, port);
                connected = true;
                lock.notifyAll();
            }

            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            synchronized (lock) {
                connected = false;
                lock.notifyAll();
            }

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
            //TODO: cannot print like this (not good in GUI)
            PrintUtils.printFromCommand(Color.RED + "Connection to server has crashed, please reconnect",
                    0, -1, true);

            disconnect();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream = new ObjectOutputStream(server.getOutputStream());

            while (isConnected()) {
                if (serverHandler != null) {
                    Object object = objectInputStream.readObject();
                    ((Packet<ServerHandler>) object).processPacket(serverHandler);
                }
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            //TODO: cannot print like this (not good in GUI)
            e.printStackTrace();

            PrintUtils.printFromCommand(Color.RED + "Connection to server has crashed, please reconnect",
                    0, -1, true);

            disconnect();
        }
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public boolean isConnected() {
        synchronized (lock) {
            return connected;
        }
    }
}
