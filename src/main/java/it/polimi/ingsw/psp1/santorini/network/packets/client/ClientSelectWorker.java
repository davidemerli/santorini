package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
