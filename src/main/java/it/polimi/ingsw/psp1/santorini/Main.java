package it.polimi.ingsw.psp1.santorini;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.gui.Gui;
import it.polimi.ingsw.psp1.santorini.gui.GuiServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.Server;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;

import java.util.List;

/**
 * Controls arguments and starts clients, server and gui or cli
 */
public class Main {

    /**
     * Main method
     *
     * @param args jar argument
     */
    public static void main(String[] args) {
        List<String> arguments = List.of(args);

        try {
            if (arguments.contains("--help") || arguments.contains("-h")) {
                System.out.println("Usage: java -jar santorini.jar [OPTION]...");
                System.out.println("Launching without options will load the Graphical User Interface (GUI)");
                System.out.println();

                System.out.println("-S, --server        starts a server on localhost, " +
                        "if no port is specified it will be 34567");
                System.out.println("-P, --port      specifies the server port " +
                        "(checked only if --server option is called)");
                System.out.println("-C, --cli       starts a new client with Command Line Interface (CLI)");
                return;
            }

            if (arguments.contains("--server") || arguments.contains("-S")) {
                int port = 34567;

                if (arguments.contains("--port")) {
                    port = Integer.parseInt(arguments.get(arguments.indexOf("--port")));
                } else if (arguments.contains("-P")) {
                    port = Integer.parseInt(arguments.get(arguments.indexOf("-P")));
                }

                Server server = new Server(port);
                new Thread(server).start();
            } else {
                Client client = new Client();
                ServerHandler clientServerHandler;

                if (arguments.contains("--cli") || arguments.contains("-C")) {
                    clientServerHandler = new CLIServerHandler(client);
                } else {
                    clientServerHandler = new GuiServerHandler(client);
                    Gui.launch(args);
                    Runtime.getRuntime().addShutdownHook(new Thread(client::disconnect));
                }

                client.setServerHandler(clientServerHandler);
            }
        } catch (Exception ex) {
            System.out.println("There were errors parsing arguments. Launch with --help for more info.");
        }
    }
}