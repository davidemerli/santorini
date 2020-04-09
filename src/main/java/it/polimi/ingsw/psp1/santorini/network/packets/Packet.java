package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.network.NetHandler;

public interface Packet<T extends NetHandler> {

    void processPacket(T netHandler);

}
