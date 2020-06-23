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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientConnectionHandler extends Observable<ConnectionObserver> implements Runnable, ClientHandler {

    private final ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

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

    /**
     * Sends a packet without delay
     *
     * @param packet to send
     */
    public void sendPacket(Packet<ServerHandler> packet) {
        sendPacket(packet, 0);
    }

    /**
     * Sends a packet with delay
     *
     * @param packet to send
     * @param delay in milliseconds
     */
    public void sendPacket(Packet<ServerHandler> packet, int delay) {
        if (closed) {
            return;
        }

        pool.schedule(() -> {
            try {
                if (!(packet instanceof ServerKeepAlive)) {
                    System.out.println("sent " + packet.toString());
                }

                objectOutputStream.writeObject(packet);
            } catch (IOException e) {
                closeConnection();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Available at the reception of the packet from server
     * Prints received packet from server
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ClassCastException
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            //TODO: Provare a spostare nel constructor
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (!closed) {
                Object object = objectInputStream.readObject();

                if (!(object instanceof ClientKeepAlive)) {
                    System.out.println("received " + object.toString());
                }

                ((Packet<ClientHandler>) object).processPacket(this);
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            closeConnection();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param packet to send
     */
    @Override
    public void handlePlayerSetName(ClientSetName packet) {
        player = new Player(packet.getName());
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param packet to send
     * @throws UnsupportedOperationException
     */
    @Override
    public void handleCreateGame(ClientCreateGame packet) {
        try {
            server.createGame(this, packet.getPlayerNumber());
        } catch (UnsupportedOperationException e) {
            sendPacket(new ServerInvalidPacket(e.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Joins game with name's room or joins the queue
     *
     * @param packet to send
     * @throws IllegalStateException
     */
    @Override
    public void handleJoinGame(ClientJoinGame packet) {
        try {
            if (packet.getGameRoom() != null) {
                server.joinGame(this, packet.getGameRoom());
            } else {
                server.joinQueue(this, packet.getPlayerNumber());
            }
        } catch (IllegalStateException ex) {
            sendPacket(new ServerInvalidPacket(ex.getMessage()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifies all players
     *
     * @param packet to send
     */
    @Override
    public void handlePowerChoosing(ClientChoosePower packet) {
        notifyObservers(o -> o.processPowerList(packet.getPowers()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifies all players
     *
     * @param packet to send
     */
    @Override
    public void handleSquareSelect(ClientSelectSquare packet) {
        notifyObservers(o -> o.processSquareSelection(packet.getSquare()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifies all players
     *
     * @param packet to send
     */
    @Override
    public void handleInteractionToggle() {
        notifyObservers(ConnectionObserver::processToggleInteraction);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifies all players
     *
     * @param packet to send
     */
    @Override
    public void handlePlayerForfeit() {
        notifyObservers(ConnectionObserver::handlePlayerForfeit);
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param packet to send
     */
    @Override
    public void handleKeepAlive() {
        sendPacket(new ServerKeepAlive(), 1000);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifies all players
     *
     * @param packet to send
     */
    @Override
    public void handleUndo() {
        notifyObservers(ConnectionObserver::processUndo);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifes all players
     *
     * @param packet to send
     */
    @Override
    public void handleRequestGameData() {
        notifyObservers(ConnectionObserver::processRequestGameData);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifes all players
     *
     * @param packet to send
     */
    @Override
    public void handleWorkerSelection(ClientSelectWorker packet) {
        notifyObservers(o -> o.processWorkerSelection(packet.getWorkerPosition()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notifes all players
     *
     * @param packet to send
     */
    @Override
    public void handleSelectStartingPlayer(ClientSelectStartingPlayer packet) {
        notifyObservers(o -> o.processStartingPlayerSelection(packet.getName()));
    }

    /**
     * Closes connection between client and server
     *
     * @throws IOException
     */
    public void closeConnection() {
        closed = true;
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }

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
