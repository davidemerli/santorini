package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientChoosePower;

import java.util.List;
import java.util.Optional;

/**
 * Used to select god from a given list
 */
public class CommandSelectPower extends Command {

    /**
     * Generic constructor
     * Defines the command name, the description, the types of argument and all alias
     */
    public CommandSelectPower() {
        super("selectpower",
                "Selects a power from a given list",
                "<power-name>/<power-id>",
                "(\\w+)|(\\d+)",
                List.of("sp", "power"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Argument is the number of the god in the list or the name of the god
     * Checks argument and select the corresponding god
     */
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

    /**
     * Checks if the string is an integer value
     * @param string to check
     * @return true if the string is an integer value
     */
    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
