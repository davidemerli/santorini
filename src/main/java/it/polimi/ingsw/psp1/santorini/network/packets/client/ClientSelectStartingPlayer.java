package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ClientSelectStartingPlayer implements Packet<ClientHandler> {

    private final String name;

    public ClientSelectStartingPlayer(String name) {
        this.name = name;
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleSelectStartingPlayer(this);
    }

    public String getName() {
        return this.name;
    }
}
