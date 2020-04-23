package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.network.NetHandler;

import java.io.Serializable;

public interface Packet<T extends NetHandler> extends Serializable {

    void processPacket(T netHandler);

}
