package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to obtain the god description
 */
public class CommandDescription extends Command {

    /**
     * Defines the command name, the description, the types of argument and all aliases
     */
    public CommandDescription() {
        super("description",
                "Shows the selected God's description",
                "<power-name>/<power-index>",
                "(\\w+)|(\\d+)",
                List.of("d", "desc"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Prints god's description
     * Argument is the position of the god in the god list or the name of the god
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (!client.isConnected()) {
            return "You are not connected.";
        }

        Power power;

        //if the serverHandler.powerList is empty it means that powers have been chosen for the game
        //and the commands tries to get the powers that have been chosen from the player list
        List<Power> powerList = serverHandler.getPowerList().isEmpty() ?
                serverHandler.getPlayerDataList().stream().map(PlayerData::getPower).collect(Collectors.toList()) :
                serverHandler.getPowerList();

        if (powerList.size() <= 0) {
            return "You're not in a game";
        }

        if (isNumeric(arguments[0])) {
            int i = Integer.parseInt(arguments[0]) - 1;

            if (i < 0 || i >= powerList.size()) {
                return "Invalid power index";
            }

            power = powerList.get(i);
        } else {
            Optional<Power> optPower = powerList.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(arguments[0]))
                    .findFirst();

            if (optPower.isEmpty()) {
                return "Wrong power name";
            }

            power = optPower.get();
        }

        PrintUtils.printPowerInfo(power);

        return String.format("Requested info about '%s'", Color.RED + power.getName() + Color.RESET);
    }
}
