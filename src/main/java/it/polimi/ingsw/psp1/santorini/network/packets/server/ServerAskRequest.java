package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

public class ServerAskRequest implements Packet<ServerHandler> {

    private final EnumRequestType requestType;

    /**
     * Generic constructor using a type request
     * @param requestType type of the request
     */
    public ServerAskRequest(EnumRequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleRequest(this);
    }

    public EnumRequestType getRequestType() {
        return requestType;
    }

    @Override
    public String toString() {
        return toString(requestType);
    }
}
