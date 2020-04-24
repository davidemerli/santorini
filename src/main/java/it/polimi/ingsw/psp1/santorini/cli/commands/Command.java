package it.polimi.ingsw.psp1.santorini.cli.commands;

import it.polimi.ingsw.psp1.santorini.cli.CLIServerHandler;
import it.polimi.ingsw.psp1.santorini.network.Client;

import java.util.List;

public abstract class Command {

    private final String command, desc, usage, pattern;

    private final List<String> aliases;

    public Command(String command, String desc, String usage, String pattern, List<String> aliases) {
        this.command = command;
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
        return command;
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
        return "";
    }
}
