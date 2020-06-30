package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing selected worker
 */
public class ClientSelectWorker implements Packet<ClientHandler> {

    private final Point position;

    /**
     * Generic constructor using a worker position
     * @param position of the worker
     */
    public ClientSelectWorker(Point position) {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleWorkerSelection(this);
    }

    public Point getWorkerPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(position);
    }
}
