package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.*;

public class CommandManager implements Runnable {

    private final List<Command> commandList;
    private final CLIServerHandler serverHandler;
    private final Client client;

    public CommandManager(Client client, CLIServerHandler serverHandler) {
        this.commandList = new ArrayList<>();
        this.client = client;
        this.serverHandler = serverHandler;
        this.addCMDs();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String result = runCommand(scanner.nextLine());
            PrintUtils.printCommand();
//            PrintUtils.clearFrom(PrintUtils.getCommandCoords().y + 1);
            PrintUtils.printFromCommand("Last action: " + result, 0, 2, true);
        }
    }

    public String runCommand(String input) {
        String[] arguments = input.split(" ");

        if (arguments.length > 0) {
            String cmd = arguments[0];
            Optional<Command> command = getCommand(cmd);

            if (command.isPresent()) {
                //if (input.matches(command.get().getPattern())) {
                try {
                    String[] subarray = Arrays.copyOfRange(arguments, 1, arguments.length);

                    return command.get().onCommand(client, serverHandler, input, subarray);
                } catch (Exception ex) {
                    String[] subarray = Arrays.copyOfRange(arguments, 1, arguments.length);

                    return "exception: " + ex.getClass() + " " + ex.getMessage() + " " + input + subarray.length;
                }
                //}
//                return "Invalid argument, the usage for this command is: ";
            }
        }

        return "Invalid command, type help for the list of commands";
    }


    public Optional<Command> getCommand(String command) {
        return commandList.stream()
                .filter(c -> c.getName().equalsIgnoreCase(command) || c.getAliases().contains(command.toLowerCase()))
                .findFirst();
    }

    public void addCMDs() {
        commandList.add(new CommandCreateGame());
        commandList.add(new CommandSurrender());
        commandList.add(new CommandHelp());
        commandList.add(new CommandInteract());
        commandList.add(new CommandPlaceWorker());
        commandList.add(new CommandReload());
        commandList.add(new CommandSelect());
        commandList.add(new CommandSelectPower());
        commandList.add(new CommandSelectStartingPlayer());
        commandList.add(new CommandSelectWorker());
        commandList.add(new CommandDescription());
    }
}

