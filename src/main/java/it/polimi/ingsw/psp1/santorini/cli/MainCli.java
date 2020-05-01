package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.network.Client;

public class MainCli {

    public static void main(String[] args) {
        Client client = new Client();
        client.setServerHandler(new CLIServerHandler(client));

        PrintUtils.firstClear();
        PrintUtils.print("Welcome to");
        PrintUtils.print("\n\n" +
                "███████╗ █████╗ ███╗   ██╗████████╗ ██████╗ ██████╗ ██╗███╗   ██╗██╗\n" +
                "██╔════╝██╔══██╗████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗██║████╗  ██║██║\n" +
                "███████╗███████║██╔██╗ ██║   ██║   ██║   ██║██████╔╝██║██╔██╗ ██║██║\n" +
                "╚════██║██╔══██║██║╚██╗██║   ██║   ██║   ██║██╔══██╗██║██║╚██╗██║██║\n" +
                "███████║██║  ██║██║ ╚████║   ██║   ╚██████╔╝██║  ██║██║██║ ╚████║██║\n" +
                "╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝\n" +
                "                                                                    \n");
        PrintUtils.print(String.format("use command '%s %s %s' to connect to a server\n",
                Color.RED + "connect", Color.GREEN + "<server-ip>", "<server-port>" + Color.RESET));

        PrintUtils.print(String.format("after connecting use command '%s %s %s' to create a new game, \n" +
                        "or wait the server to assign you to a new game\n",
                Color.RED + "create", Color.GREEN + "<game-name>", "<player-number>" + Color.RESET));

        PrintUtils.print(String.format("use command '%s' to get a list of commands",
                Color.RED + "help" + Color.RESET));

        PrintUtils.printCommand();
    }
}
