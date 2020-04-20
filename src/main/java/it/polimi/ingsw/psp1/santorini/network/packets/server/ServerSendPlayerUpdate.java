package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerSendPlayerUpdate implements Packet<ServerHandler> {

    private final PlayerData playerData;
    private final EnumTurnState playerState;
    private final boolean hasWon;
    private final boolean hasLost;

    public ServerSendPlayerUpdate(PlayerData playerData, EnumTurnState playerState, boolean hasWon, boolean hasLost) {
        this.playerData = playerData;
        this.playerState = playerState;
        this.hasWon = hasWon;
        this.hasLost = hasLost;
    }

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

    public boolean hasWon() {
        return hasWon;
    }

    public boolean hasLost() {
        return hasLost;
    }
}
