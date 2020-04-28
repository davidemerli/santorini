package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerAskRequest;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerInvalidPacket;
import it.polimi.ingsw.psp1.santorini.observer.ConnectionObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ClientConnectionHandler extends Observer<ConnectionObserver> implements Runnable, ClientHandler {

    private final Server server;

    private final Socket clientSocket;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private Player player;
    private boolean closed;

    ClientConnectionHandler(Server server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        sendPacket(new ServerAskRequest(EnumRequestType.SELECT_NAME));
    }

    public void sendPacket(Packet<ServerHandler> packet) {
        if (closed) {
            return;
        }

        try {
            System.out.println("sent " + packet);
            objectOutputStream.writeObject(packet);
        } catch (IOException e) {
            //TODO: what if send packet fails? try to resend?
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (!closed) {
                Object object = objectInputStream.readObject();
                System.out.println("received " + object);
                ((Packet<ClientHandler>) object).processPacket(this);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            closeConnection();
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
        //TODO: send another keep alive
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

    public void closeConnection() {
        closed = true;

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.disconnectClient(this);

        notifyObservers(ConnectionObserver::handleCloseConnection);
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    public boolean isClosed() {
        return closed;
    }

}
