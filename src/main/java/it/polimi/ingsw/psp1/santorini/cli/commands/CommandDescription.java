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

public class CommandDescription extends Command {

    public CommandDescription() {
        super("description",
                "Shows the selected God's description",
                "<power-name>",
                "",
                List.of("d", "desc"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        Power power;

        List<Power> powerList = serverHandler.getPowerList().size() != 0 ?
                serverHandler.getPowerList() :
                serverHandler.getPlayerDataList().stream().map(PlayerData::getPower).collect(Collectors.toList());

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

    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
