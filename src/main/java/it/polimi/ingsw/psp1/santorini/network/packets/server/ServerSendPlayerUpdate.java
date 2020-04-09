package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerSendPlayerUpdate implements Packet<ServerHandler> {

    private final String playerName;
    private final EnumTurnState playerState;

    public ServerSendPlayerUpdate(String playerName, EnumTurnState playerState) {
        this.playerName = playerName;
        this.playerState = playerState;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerUpdate(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    public EnumTurnState getPlayerState() {
        return playerState;
    }
}
