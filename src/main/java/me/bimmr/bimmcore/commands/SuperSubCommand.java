package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.messages.FancyMessage;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Randy on 07/03/16.
 */
public abstract class SuperSubCommand extends SubCommand {

    public SuperCommand superCommand;

    public SuperSubCommand(SuperCommand superCommand, String name) {
        super(name);
        this.superCommand = superCommand;
    }

    @Override
    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public abstract String getCommandExample();

    @Override
    public abstract FancyMessage getFancyMessage();

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public abstract List<String> getTabs(String[] args);
}
