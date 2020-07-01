package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.cli.CommandManager;
import it.polimi.ingsw.psp1.santorini.cli.PrintUtils;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to show the complete list of command
 */
public class CommandHelp extends Command {

    /**
     * Defines the command name, the description, the types of argument and all aliases
     */
    public CommandHelp() {
        super("help",
                "Shows the list of commands",
                "",
                "(.)*",
                List.of("h"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Prints helps
     */
    @Override
    public String onCommand(Client client, CLIServerHandler serverHandler, String input, String[] arguments) {
        if (arguments.length == 0) {
            String commands = CommandManager.getInstance().getCommandList().stream()
                    .map(Command::getName).collect(Collectors.joining(", "));

            PrintUtils.printFromCommand(commands, 0, -4, true);
            return "Requested all commands";
        }

        Optional<Command> command = CommandManager.getInstance().getCommand(arguments[0]);

        if(command.isEmpty()) {
            return "Command not found";
        }

        PrintUtils.printFromCommand(command.get().getName(), 0, -4, true);
        PrintUtils.printFromCommand(command.get().getDesc(), 0, -5, true);
        PrintUtils.printFromCommand(command.get().getUsage(), 0, -6, true);

        return "Requested info about command " + arguments[0];
    }
}
