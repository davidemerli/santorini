package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private boolean running;

    private ObjectOutputStream objectOutputStream;

    private final ServerHandler serverHandler;

    private String ip;
    private int port;

    private Socket server;

    public Client(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
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
            if(objectOutputStream == null) {
                objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            }

            objectOutputStream.writeObject(packet);

            System.out.println("sent " + packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try (Socket server = new Socket(ip, port)){
            this.server = server;

            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());

            while(true) {

                Object object = objectInputStream.readObject();
                System.out.println(object);
                ((Packet<ServerHandler>) object).processPacket(serverHandler);
            }
        } catch(IOException | ClassNotFoundException | ClassCastException e){
            e.printStackTrace();
        }
    }
}
