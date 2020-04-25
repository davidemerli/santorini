package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientChoosePower;

import java.util.Arrays;
import java.util.Optional;

public class CommandSelectPower extends Command {

    public CommandSelectPower() {
        super("selectpower",
                "Selects a power from a given list",
                "<power-name> / <power-id>",
                "",
                Arrays.asList("sp", "power"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if(isNumeric(arguments[0])) {
            int index = Integer.parseInt(arguments[0]);

            if(index < 0 || index >= serverHandler.getPowerList().size()) {
                return "Invalid index";
            }

            Power power = serverHandler.getPowerList().get(index);
            client.sendPacket(new ClientChoosePower(power));

            return String.format("Selected power: '%s'", power.getClass().getSimpleName());
        }

        Optional<Power> power = serverHandler.getPowerList().stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(arguments[0]))
                .findFirst();

        if(power.isPresent()) {
            client.sendPacket(new ClientChoosePower(power.get()));

            return String.format("Selected power: '%s'", power.get().getClass().getSimpleName());
        }

        return "Invalid name";
    }

    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
