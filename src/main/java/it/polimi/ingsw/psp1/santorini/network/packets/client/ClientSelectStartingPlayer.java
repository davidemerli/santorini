package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Client packet containing the first player who will play
 */
public class ClientSelectStartingPlayer implements Packet<ClientHandler> {

    private final String name;

    /**
     * Generic constructor using the name of the player
     * @param name of the player
     */
    public ClientSelectStartingPlayer(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handleSelectStartingPlayer(this);
    }

    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(name);
    }
}
