package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientSetName implements Packet<ClientHandler> {

    private final String name;

    public ClientSetName(String name) {
        this.name = name;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handlePlayerSetName(this);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return toString(name);
    }
}
