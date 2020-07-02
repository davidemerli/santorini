package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;

/**
 * Defines the structure of a generic command
 */
public abstract class Command {

    private final String cmdName;
    private final String desc;
    private final String usage;
    private final String pattern;

    private final List<String> aliases;

    /**
     * Defines a new command by command name, command description, usage, the pattern and all aliases
     * The pattern is used to validate commands with regex
     *
     * @param cmdName name of the command
     * @param desc    description of the command
     * @param usage   usage of the command
     * @param pattern pattern argument of the command
     * @param aliases all command aliases
     */
    public Command(String cmdName, String desc, String usage, String pattern, List<String> aliases) {
        this.cmdName = cmdName;
        this.desc = desc;
        this.usage = usage;
        this.pattern = pattern;
        this.aliases = aliases;
    }

    /**
     * Analyzes client input and returns a response
     *
     * @param client        current client
     * @param serverHandler current serverHandler
     * @param input         client input
     * @param arguments     input arguments
     * @return command response
     */
    public abstract String onCommand(Client client, CLIServerHandler serverHandler,
                                     String input, String[] arguments);

    public List<String> getAliases() {
        return aliases;
    }

    public String getName() {
        return cmdName;
    }

    public String getDesc() {
        return desc;
    }

    public String getUsage() {
        return usage;
    }

    public String getPattern() {
        return pattern;
    }

    public String getHelp() {
        //TODO
        return "";
    }

    /**
     * Checks if the string is an integer value
     *
     * @param string to check
     * @return true if the string is an integer value
     */
    protected boolean isNumeric(String string) {
        return string.matches("\\d+");
    }
}
