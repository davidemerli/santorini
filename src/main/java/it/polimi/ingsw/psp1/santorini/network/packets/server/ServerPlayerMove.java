package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerPlayerMove implements Packet<ServerHandler> {

    private final PlayerData playerData;
    private final EnumMoveType moveType;

    public ServerPlayerMove(PlayerData playerData, EnumMoveType moveType) {
        this.playerData = playerData;
        this.moveType = moveType;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerMove(this);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public EnumMoveType getMoveType() {
        return moveType;
    }
}
