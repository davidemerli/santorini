package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.cli.commands.*;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.*;

/**
 * Manages all user commands
 */
public class CommandManager {

    private static CommandManager instance;

    private final List<Command> commandList;

    /**
     * Generic constructor
     * Creates a command list and adds all commands
     */
    private CommandManager() {
        this.commandList = new ArrayList<>();
        this.addCMDs();
    }

    /**
     * @return command manager instance
     */
    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }

        return instance;
    }

    /**
     * Analyzes command inserted by client and run it
     *
     * @param client        current client
     * @param serverHandler current serverHandler
     * @param input         client input
     * @return command response
     */
    public String runCommand(Client client, CLIServerHandler serverHandler, String input) {
        String[] arguments = input.split(" ");

        if (arguments.length > 0) {
            String cmd = arguments[0];
            Optional<Command> command = getCommand(cmd);

            if (command.isPresent()) {
                String[] subarray = Arrays.copyOfRange(arguments, 1, arguments.length);

                if (input.substring(arguments[0].length()).strip().matches(command.get().getPattern())) {
                    try {
                        return command.get().onCommand(client, serverHandler, input, subarray);
                    } catch (Exception ex) {
                        return "exception: " + ex.getClass() + " " + ex.getMessage() + " " + input + " " + subarray.length;
                    }
                }
                return String.format("Invalid argument, the usage for this command is: '%s %s'",
                        Color.BLUE + command.get().getName() + Color.RESET,
                        Color.RED + command.get().getUsage() + Color.RESET);
            }
        }

        return "Invalid command, type help for the list of commands";
    }

    /**
     * Gets a command in a Optional if present
     *
     * @param command to get
     * @return a Optional with command if present
     */
    public Optional<Command> getCommand(String command) {
        return commandList.stream()
                .filter(c -> c.getName().equalsIgnoreCase(command) || c.getAliases().contains(command.toLowerCase()))
                .findFirst();
    }

    /**
     * Adds all type of commands
     */
    private void addCMDs() {
        commandList.add(new CommandConnect());
        commandList.add(new CommandCreateGame());
//        commandList.add(new CommandSurrender());
        commandList.add(new CommandHelp());
        commandList.add(new CommandInteract());
        commandList.add(new CommandJoinGame());
        commandList.add(new CommandPlaceWorker());
        commandList.add(new CommandReload());
        commandList.add(new CommandSelect());
        commandList.add(new CommandSelectPower());
        commandList.add(new CommandSelectStartingPlayer());
        commandList.add(new CommandSelectWorker());
        commandList.add(new CommandSetName());
        commandList.add(new CommandDescription());
        commandList.add(new CommandUndo());
    }

    /**
     * @return the list of all commands
     */
    public List<Command> getCommandList() {
        return Collections.unmodifiableList(commandList);
    }
}

