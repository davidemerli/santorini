package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientCreateGame;

import java.util.Arrays;
import java.util.List;

public class CommandConnect extends Command {

    public CommandConnect() {
        super("connect",
                "Connects to server",
                "<server-ip> <port>",
                "",
                List.of("cn"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
//        String ip = arguments[0];
//        int port = Integer.parseInt(arguments[1]);

        client.connectToServer("localhost", 34567);

        return String.format("Trying connection to '%s':'%d'", "localhost", 34567);
    }
}
