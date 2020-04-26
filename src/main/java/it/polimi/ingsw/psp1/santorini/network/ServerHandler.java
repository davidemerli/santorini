package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

public interface ServerHandler extends NetHandler {
    void handleKeepAlive(ServerKeepAlive packet);

    void handleGameData(ServerGameData packet);

    void handleRequest(ServerAskRequest packet);

    void handlePlayerUpdate(ServerSendPlayerUpdate packet);

    void handleReceivedMoves(ServerMovePossibilities packet);

    void handleError(ServerInvalidPacket packet);

    void handlePlayerMove(ServerPlayerMove serverPlayerMove);

    void handlePowerList(ServerPowerList serverPowerList);
}
