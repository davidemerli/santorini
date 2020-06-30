package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Server packet containing list with all available gods
 */
public class ServerPowerList implements Packet<ServerHandler> {

    private final List<Power> availablePowers;
    private final int toSelect;

    /**
     * Generic constructor
     *
     * @param availablePowers list with available gods
     * @param toSelect        number of gods to be selected
     */
    public ServerPowerList(List<Power> availablePowers, int toSelect) {
        this.availablePowers = availablePowers;
        this.toSelect = toSelect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePowerList(this);
    }

    public List<Power> getAvailablePowers() {
        return availablePowers;
    }

    public int getToSelect() {
        return toSelect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(availablePowers.stream().map(Power::getName).collect(Collectors.joining(", ")), toSelect);
    }
}
