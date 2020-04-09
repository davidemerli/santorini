package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.awt.*;

public class ClientSelectSquare implements Packet<ClientHandler> {

    private final Point selectedSquare;

    public ClientSelectSquare(Point selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleSquareSelect(this);
    }

    public Point getSquare() {
        return this.selectedSquare;
    }
}
