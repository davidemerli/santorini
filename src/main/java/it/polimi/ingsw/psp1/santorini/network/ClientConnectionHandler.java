package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerAskRequest;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerInvalidPacket;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerKeepAlive;
import it.polimi.ingsw.psp1.santorini.observer.ConnectionObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ClientConnectionHandler extends Observable<ConnectionObserver> implements Runnable, ClientHandler {

    private final Server server;

    private final Socket clientSocket;

    private final ObjectOutputStream objectOutputStream;
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
            closeConnection();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            //TODO: Provare a spostare nel constructor
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
        //TODO: recheck if name is already present
    }

    @Override
    public void handleCreateGame(ClientCreateGame packet) {
        try {
            server.createGame(this, packet.getPlayerNumber());
        } catch (UnsupportedOperationException e) {
            sendPacket(new ServerInvalidPacket(e.getMessage()));
        }
    }

    @Override
    public void handleJoinGame(ClientJoinGame packet) {
        if (packet.getGameRoom() != -1) {
            server.joinGame(this, packet.getGameRoom());
        } else {
            server.joinQueue(this, packet.getPlayerNumber());
        }
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
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                sendPacket(new ServerKeepAlive());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
            if (objectInputStream != null) {
                objectInputStream.close();
            }

            objectOutputStream.close();
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
