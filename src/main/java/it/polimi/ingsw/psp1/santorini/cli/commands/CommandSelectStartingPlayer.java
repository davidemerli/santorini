package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSelectStartingPlayer;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;

import java.util.List;
import java.util.Optional;

public class CommandSelectStartingPlayer extends Command {

    public CommandSelectStartingPlayer() {
        super("startingplayer",
                "Selects the starting player",
                "<player-name> / <player-id>",
                "",
                List.of("start", "begin"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (isNumeric(arguments[0])) {
            int index = Integer.parseInt(arguments[0]) - 1;

            if (index < 0 || index >= serverHandler.getPlayerDataList().size()) {
                return "Invalid index";
            }

            PlayerData player = serverHandler.getPlayerDataList().get(index);
            client.sendPacket(new ClientSelectStartingPlayer(player.getName()));

            return String.format("Selected starting player: '%s'", player.getName());
        }

        Optional<PlayerData> player = serverHandler.getPlayerDataList().stream()
                .filter(p -> p.getName().equalsIgnoreCase(arguments[0]))
                .findFirst();

        if (player.isPresent()) {
            client.sendPacket(new ClientSelectStartingPlayer(player.get().getName()));

            return String.format("Selected starting player: '%s'", player.get().getName());
        }

        return "Invalid name";
    }

    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
