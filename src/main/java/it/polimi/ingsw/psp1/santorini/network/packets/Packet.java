package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.network.NetHandler;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Defines a generic packet
 *
 * @param <T> NetHandler type to distinguish if a packet needs to be handled by Server or Client
 */
public interface Packet<T extends NetHandler> extends Serializable {

    /**
     * Process packets
     *
     * @param netHandler client or server netHandler
     */
    void processPacket(T netHandler);

    /**
     * Returns a string representation of the packet values
     *
     * @param values that needs to be shown
     * @return packet string representation
     */
    default String toString(Object... values) {
        return getClass().getSimpleName() + Arrays.toString(values);
    }
}
