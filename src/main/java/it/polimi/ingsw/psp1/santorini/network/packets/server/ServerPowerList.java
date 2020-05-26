package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;

public class ServerPowerList implements Packet<ServerHandler> {

    private final List<Power> availablePowers;
    private final int toSelect;

    public ServerPowerList(List<Power> availablePowers, int toSelect) {
        this.availablePowers = availablePowers;
        this.toSelect = toSelect;
    }

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
}
