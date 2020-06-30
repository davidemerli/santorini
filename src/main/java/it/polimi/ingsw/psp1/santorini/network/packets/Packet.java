package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.network.NetHandler;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Defines a generic packet
 *
 * @param <T>
 */
public interface Packet<T extends NetHandler> extends Serializable {

    /**
     * Process packets
     *
     * @param netHandler valid netHandler
     */
    void processPacket(T netHandler);

    default String toString(Object... values) {
        return getClass().getSimpleName() + Arrays.toString(values);
    }
}
