package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientKeepAlive;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerKeepAlive;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Defines the functions of a generic client who can run in parallel with other ones
 * Client can connect to serve using a port and a ip address
 */
public class Client implements Runnable {

    private final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    private ObjectOutputStream objectOutputStream;
    private ServerHandler serverHandler;
    private Socket server;

    private boolean connected;

    private boolean debug;

    /**
     * Sets connection between client and server
     * Creates a new thread for every player
     *
     * @param ip   server ip
     * @param port socket port
     */
    public void connectToServer(String ip, int port) {
        if (connected) {
            disconnect();
        }

        try {
            server = new Socket(ip, port);
            connected = true;

            new Thread(this).start(); //TODO: maybe use pool
        } catch (IOException e) {
            serverHandler.onConnectionFail();
        }
    }

    /**
     * Interrupts connection between client and server
     */
    public void disconnect() {
        try {
            connected = false;

            if (server != null) {
                server.close();
                server = null;
            }

            serverHandler.onDisconnect();
            serverHandler.reset();//TODO: check if fine on CLI
        } catch (IOException e) {
            System.out.println("Server connection closed wrongly");
        }
    }

    /**
     * Sends packet to server from client
     *
     * @param packet to send
     */
    public void sendPacket(Packet<ClientHandler> packet) {
        pool.submit(() -> {
            try {
                objectOutputStream.writeObject(packet);
                objectOutputStream.flush();

                if (debug && !(packet instanceof ClientKeepAlive)) {
                    System.out.println("sent: " + packet.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                disconnect();
            }
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Available to receive packets from server
     * Prints packets from server
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream = new ObjectOutputStream(server.getOutputStream());

            while (isConnected()) {
                if (serverHandler != null) {
                    Packet<ServerHandler> packet = (Packet<ServerHandler>) objectInputStream.readObject();
                    packet.processPacket(serverHandler);

                    if (debug && !(packet instanceof ServerKeepAlive)) {
                        System.out.println("received: " + packet.toString());
                    }
                }
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /**
     * Debug function
     */
    public void enableDebug() {
        debug = true;
    }

    public boolean isConnected() {
        return connected;
    }
}
