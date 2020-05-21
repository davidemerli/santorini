package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;

public abstract class Command {

    private final String cmdName;
    private final String desc;
    private final String usage;
    private final String pattern;

    private final List<String> aliases;

    public Command(String cmdName, String desc, String usage, String pattern, List<String> aliases) {
        this.cmdName = cmdName;
        this.desc = desc;
        this.usage = usage;
        this.pattern = pattern;
        this.aliases = aliases;
    }

    public abstract String onCommand(Client client, CLIServerHandler serverHandler,
                                     String input, String[] arguments) throws Exception;

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
        //TODO: add a custom pattern for every command
        return pattern;
    }

    public String getHelp() {
        //TODO
        return "";
    }
}
