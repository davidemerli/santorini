package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientChoosePower implements Packet<ClientHandler> {

    private final List<Power> powers;

    public ClientChoosePower(List<Power> chosenPowers) {
        this.powers = new ArrayList<>(chosenPowers);
    }

    public ClientChoosePower(Power power) {
        this(Collections.singletonList(power));
    }

    @Override
    public void processPacket(ClientHandler netHandler) {
        netHandler.handlePowerChoosing(this);
    }

    public List<Power> getPowers() {
        return this.powers;
    }
}
