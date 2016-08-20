package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.messages.FancyMessage;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 07/03/16.
 */
public abstract class SuperCommand extends SubCommand {

    private List<SubCommand> subCommands;

    public SuperCommand(String name) {
        super(name);
        subCommands = new ArrayList<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (args.length > 1) {
            if (getSubCommand(args[1]) != null) {
                getSubCommand(args[1]).execute(sender, args);
            } else
                sender.sendMessage(getInvalidSubCommandMessage());
        } else if (sender instanceof Player)
            sendAllSubFancyMessage(sender);
        else
            sender.sendMessage(getAllSubCommandExample());
    }

    public abstract String getInvalidSubCommandMessage();

    @Override
    public abstract List<String> getAliases();

    public abstract String getCommandExampleHeader();

    public String getAllSubCommandExample() {
        String example = "";

        for (int i = 0; i < subCommands.size(); i++) {
            SubCommand subCommand = subCommands.get(i);
            if (i != 0 && i != subCommands.size() - 1)
                example += "\n";
            example += subCommand.getCommandExample();
        }

        return getCommandExampleHeader() + "\n" + example;
    }

    public void sendAllSubFancyMessage(CommandSender sender) {
        sender.sendMessage(getCommandExampleHeader());

        for (int i = 0; i < subCommands.size(); i++)
            subCommands.get(i).getFancyMessage().send((Player) sender);
    }

    @Override
    public abstract FancyMessage getFancyMessage();

    @Override
    public abstract String getCommandExample();

    @Override
    public abstract String getPermission();

    @Override
    public List<String> getTabs(String[] args) {

        if (args.length == 2) {
            List<String> tabs = new ArrayList<>();
            for (SubCommand subCommand : subCommands)
                tabs.add(subCommand.getName());

            return tabs;
        } else if (args.length > 2 && getSubCommand(args[1]) != null) {
            return getSubCommand(args[1]).getTabs(args);
        }
        return null;
    }

    /**
     * Get the SubCommand that matches the command
     *
     * @param command
     * @return
     */
    public SubCommand getSubCommand(String command) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getName().equalsIgnoreCase(command))
                return subCommand;
        return null;
    }

    /**
     * Add a subcommand
     *
     * @param subCommand
     */
    public void addSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    /**
     * Remove a subcommand
     *
     * @param subCommand
     */
    public void removeSubCommand(SubCommand subCommand) {
        this.subCommands.remove(subCommand);
    }
}
