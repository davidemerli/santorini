package it.polimi.ingsw.psp1.santorini.cli.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandManager {
    private final List<Command> commandList;

    public CommandManager() {
        commandList = new ArrayList<>();
        this.addCMDs();
    }

    public void addCMDs() {
        commandList.add(new CommandBuild());
        commandList.add(new CommandHelp());
        commandList.add(new CommandInteract());
        commandList.add(new CommandMove());
        commandList.add(new CommandPlaceWorker());
        commandList.add(new CommandReload());
        commandList.add(new CommandSelectWorker());
        commandList.add(new CommandShowDescription());
        commandList.add(new CommandSurrender());

    }

    public String runCommand(String input) {
        String[] arguments = input.split(" ");
        if (arguments.length > 0) {
            String cmd = arguments[0];
            Optional<Command> command = getCommand(cmd);
            if (command.isPresent()) {
                if (input.matches(command.get().getPattern())) {
                    try {
                        String[] subarray = Arrays.copyOfRange(arguments, 1, arguments.length + 1);
                        return command.get().onCommand(input, subarray);
                    } catch (Exception ex) {
                        return ex.getMessage();
                    }
                }
                return "Invalid argument, the usage for this command is: ";
            }
        }

        return "invalid command, type help for the list of commands";
    }


    public Optional<Command> getCommand(String command) {
        for (Command c : this.commandList) {
            if (c.getName().equalsIgnoreCase(command) || c.getAliases().contains(command.toLowerCase())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}

