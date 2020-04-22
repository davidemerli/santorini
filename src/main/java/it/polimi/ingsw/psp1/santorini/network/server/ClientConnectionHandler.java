package it.polimi.ingsw.psp1.santorini.network.server;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerInvalidPacket;
import it.polimi.ingsw.psp1.santorini.observer.ConnectionObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ClientConnectionHandler implements Runnable, ClientHandler {

    private final Server server;

    private final Socket clientSocket;
    private final List<ConnectionObserver> observers;

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    private Player player;

    ClientConnectionHandler(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.observers = new ArrayList<>();

        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void sendPacket(Packet<ServerHandler> packet) {
        try {
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            //TODO: what if send packet fails? try to resend?
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            Object object = objectInputStream.readObject();

            ((Packet<ClientHandler>) object).processPacket(this);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePlayerSetName(ClientSetName packet) {
        player = new Player(packet.getName());

        try {
            server.addToWait(player, this);
        } catch (UnsupportedOperationException e) {
            sendPacket(new ServerInvalidPacket("There is a player with the same name. Please choose another name"));
        }
    }

    @Override
    public void handleCreateGame(ClientCreateGame packet) {
        try {
            //TODO: give the game custom name from packet
            server.createGame(this, packet.getPlayerNumber());
        } catch (UnsupportedOperationException e) {
            sendPacket(new ServerInvalidPacket("A game is already created"));
        }
    }

    @Override
    public void handleJoinGame(ClientJoinGame packet) {
        //Maybe useless
    }

    @Override
    public void handlePowerChoosing(ClientChoosePower clientChoosePower) {
        notifyObservers(o -> o.processPowerList(clientChoosePower.getPowers()));
    }

    @Override
    public void handleSquareSelect(ClientSelectSquare clientSelectSquare) {
        notifyObservers(o -> o.processSquareSelection(clientSelectSquare.getSquare()));
    }

    @Override
    public void handleInteractionToggle(ClientToggleInteraction clientToggleInteraction) {
        notifyObservers(ConnectionObserver::processToggleInteraction);
    }

    @Override
    public void handlePlayerForfeit(ClientForfeit clientForfeit) {
        notifyObservers(ConnectionObserver::handlePlayerForfeit);
    }

    @Override
    public void handleKeepAlive(ClientKeepAlive clientKeepAlive) {
        notifyObservers(ConnectionObserver::processKeepAlive);
    }

    @Override
    public void handleRequestGameData(ClientRequestGameData clientRequestGameData) {
        notifyObservers(ConnectionObserver::processRequestGameData);
    }

    @Override
    public void handleWorkerSelection(ClientSelectWorker clientSelectWorker) {
        notifyObservers(o -> o.processWorkerSelection(clientSelectWorker.getWorkerPosition()));
    }

    @Override
    public void handleSelectStartingPlayer(ClientSelectStartingPlayer clientSelectStartingPlayer) {
        notifyObservers(o -> o.processStartingPlayerSelection(clientSelectStartingPlayer.getName()));
    }

    public void addObserver(ConnectionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ConnectionObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Consumer<ConnectionObserver> lambda) {
        observers.forEach(lambda);
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

}
