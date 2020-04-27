package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    private String name;

    public SubCommand(String name) {
        this.name = name;
    }

    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    public abstract List<String> getAliases();

    public abstract String getCommandExample();

    public abstract FancyMessage getFancyMessage();

    public String getName() {
        return this.name;
    }

    public abstract String getPermission();

    public abstract List<String> getTabs(String[] args);

    public final boolean hasPermission(CommandSender sender) {

        return sender.hasPermission(getPermission());
    }

}
