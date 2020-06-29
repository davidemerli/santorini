package it.polimi.ingsw.psp1.santorini.network.packets.client;

import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ClientHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Client selects his god that will play
 */
public class ClientChoosePower implements Packet<ClientHandler> {

    private final List<Power> powers;

    /**
     * Generic constructor giving a list of powers
     *
     * @param chosenPowers list with all chosen gods
     */
    public ClientChoosePower(List<Power> chosenPowers) {
        this.powers = new ArrayList<>(chosenPowers);
    }

    /**
     * Generic constructor using a single god
     *
     * @param power chosen god
     */
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

    @Override
    public String toString() {
        return toString(getPowers().stream().map(Power::getName).collect(Collectors.joining(",")));
    }
}
