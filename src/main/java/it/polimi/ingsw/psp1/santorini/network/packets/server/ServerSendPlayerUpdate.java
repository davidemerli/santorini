package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

/**
 * Server packet containing players information
 */
public class ServerSendPlayerUpdate implements Packet<ServerHandler> {

    private final PlayerData playerData;
    private final EnumTurnState playerState;
    private final boolean shouldShowInteraction;

    /**
     * Generic constructor
     *
     * @param playerData            player information
     * @param playerState           player state
     * @param shouldShowInteraction true if the interaction button must be shown
     */
    public ServerSendPlayerUpdate(PlayerData playerData, EnumTurnState playerState, boolean shouldShowInteraction) {
        this.playerData = playerData;
        this.playerState = playerState;
        this.shouldShowInteraction = shouldShowInteraction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerUpdate(this);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public EnumTurnState getPlayerState() {
        return playerState;
    }

    /**
     * Manages the button
     *
     * @return true if the interaction button must be shown
     */
    public boolean shouldShowInteraction() {
        return shouldShowInteraction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(playerData, playerState, shouldShowInteraction);
    }
}
