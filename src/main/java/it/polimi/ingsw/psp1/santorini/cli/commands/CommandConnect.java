package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;
import java.util.Optional;

public class CommandConnect extends Command {

    private String lastIp;
    private String lastPort;

    public CommandConnect() {
        super("connect",
                "Connects to server, parameters not specified are inferred or saved from previous command calls",
                "<server-ip> <port>/<server-ip>/<server-port>",
                "([\\w\\\\.\\d]+ \\d+)|([\\d\\\\.\\w]+)|(^$)",
                List.of("cn"));
    }

    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (client.isConnected()) {
            return "Connection already established.";
        }

        String ip, port;

        if (arguments.length == 0) {
            ip = Optional.ofNullable(lastIp).orElse("localhost");
            port = Optional.ofNullable(lastPort).orElse("34567");
        } else if(arguments.length == 1){
            ip = arguments[0];
            port = Optional.ofNullable(lastPort).orElse("34567");
        } else {
            ip = arguments[0];
            port = arguments[1];
        }

        serverHandler.reset();
        client.connectToServer(ip, Integer.parseInt(port));

        lastIp = ip;
        lastPort = port;

        return String.format("Trying connection to '%s':'%s'", ip, port);
    }
}
