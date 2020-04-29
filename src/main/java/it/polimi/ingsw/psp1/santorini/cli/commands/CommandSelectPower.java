package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientChoosePower;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandSelectPower extends Command {

    public CommandSelectPower() {
        super("selectpower",
                "Selects a power from a given list",
                "<power-name> / <power-id>",
                "",
                List.of("sp", "power"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (!serverHandler.isYourTurn()) {
            return "Not your turn";
        }

        Power power;

        if (isNumeric(arguments[0])) {
            int index = Integer.parseInt(arguments[0]) - 1;

            if (index < 0 || index >= serverHandler.getPowerList().size()) {
                return "Invalid index";
            }

            power = serverHandler.getPowerList().get(index);
        } else {
            Optional<Power> optPower = serverHandler.getPowerList().stream()
                    .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(arguments[0]))
                    .findFirst();

            if (optPower.isPresent()) {
                power = optPower.get();
            } else {
                return "Invalid name";
            }
        }

        client.sendPacket(new ClientChoosePower(power));
        serverHandler.getPowerList().remove(power);

        PrintUtils.printPowerList(serverHandler.getPowerList());

        return String.format("Selected power: '%s'", power.getClass().getSimpleName());
    }

    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
