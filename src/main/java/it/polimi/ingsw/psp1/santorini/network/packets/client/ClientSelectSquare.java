package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing a selected square
 */
public class ClientSelectSquare implements Packet<ClientHandler> {

    private final Point selectedSquare;

    /**
     * Generic constructor using a selected square
     * @param selectedSquare selected square
     */
    public ClientSelectSquare(Point selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleSquareSelect(this);
    }

    public Point getSquare() {
        return this.selectedSquare;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(selectedSquare);
    }
}
