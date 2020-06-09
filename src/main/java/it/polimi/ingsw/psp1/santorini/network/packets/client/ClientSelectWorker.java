package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientSelectWorker implements Packet<ClientHandler> {

    private final Point position;

    public ClientSelectWorker(Point position) {
        this.position = position;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleWorkerSelection(this);
    }

    public Point getWorkerPosition() {
        return position;
    }

    @Override
    public String toString() {
        return toString(position);
    }
}
