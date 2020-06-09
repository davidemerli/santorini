package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.network.NetHandler;

import java.io.Serializable;
import java.util.Arrays;

public interface Packet<T extends NetHandler> extends Serializable {

    void processPacket(T netHandler);

    default String toString(Object... values) {
        return getClass().getSimpleName() + Arrays.toString(values);
    }
}
