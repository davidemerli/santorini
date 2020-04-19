package it.polimi.ingsw.psp1.santorini.network.server;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientConnectionHandler implements Runnable {

    private final Socket clientSocket;
    private final ExecutorService executionQueue;

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    ClientConnectionHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.executionQueue = Executors.newSingleThreadExecutor();

        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void sendPacket(Packet<ClientHandler> packet) {
        try {
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            //TODO: what if send packet fails? try to resend?
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
