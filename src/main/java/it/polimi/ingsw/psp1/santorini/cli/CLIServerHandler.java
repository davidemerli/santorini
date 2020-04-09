package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

public class CLIServerHandler implements ServerHandler {

    @Override
    public void handleKeepAlive(ServerKeepAlive packet) {

    }

    @Override
    public void handleSendGameData(ServerGameData packet) {

    }

    @Override
    public void handleRequest(ServerAskRequest packet) {

    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {

    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {

    }

    @Override
    public void handleError(ServerInvalidPacket packet) {

    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {

    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {

    }
}
