package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.gui.chat.ChatMenu;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Super command.
 */
public abstract class SuperCommand extends SubCommand {

    private List<SubCommand> subCommands;

    /**
     * Instantiates a new Super command.
     *
     * @param name the name
     */
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

    /**
     * Gets invalid sub command message.
     *
     * @return the invalid sub command message
     */
    public abstract String getInvalidSubCommandMessage();

    @Override
    public abstract List<String> getAliases();

    /**
     * Gets command example header.
     *
     * @return the command example header
     */
    public abstract String getCommandExampleHeader();

    /**
     * Gets all sub command example.
     *
     * @return the all sub command example
     */
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

    /**
     * Send all sub fancy message.
     *
     * @param sender the sender
     */
    public void sendAllSubFancyMessage(CommandSender sender) {
        ChatMenu menu = new ChatMenu(ChatColor.DARK_RED + getCommandExampleHeader(), ChatColor.DARK_GREEN, 5, ChatMenu.HeightControl.MANUAL_EXTERNAL);
        menu.setSpacedFormat(true);
        for (SubCommand command : subCommands)
            if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
                menu.addLine(command.getFancyMessage());
            }
        menu.show((Player) sender);
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
     * Gets sub command.
     *
     * @param command the command
     * @return the sub command
     */
    public SubCommand getSubCommand(String command) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getName().equalsIgnoreCase(command))
                return subCommand;
        return null;
    }

    /**
     * Add sub command.
     *
     * @param subCommand the sub command
     */
    public void addSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    /**
     * Remove sub command.
     *
     * @param subCommand the sub command
     */
    public void removeSubCommand(SubCommand subCommand) {
        this.subCommands.remove(subCommand);
    }
}
