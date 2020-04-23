package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;

public class ServerPowerList implements Packet<ServerHandler> {

    private final List<Power> availablePowers;

    public ServerPowerList(List<Power> availablePowers) {
        this.availablePowers = availablePowers;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePowerList(this);
    }

    public List<Power> getAvailablePowers() {
        return availablePowers;
    }
}
