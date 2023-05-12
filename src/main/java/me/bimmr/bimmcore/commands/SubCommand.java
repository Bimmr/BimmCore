package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The type Sub command.
 */
public abstract class SubCommand {

    private String name;

    /**
     * Instantiates a new Sub command.
     *
     * @param name the name
     */
    public SubCommand(String name) {
        this.name = name;
    }

    /**
     * Execute.
     *
     * @param sender the sender
     * @param args   the args
     * @throws CommandException the command exception
     */
    public abstract void execute(CommandSender sender, String[] args) throws CommandException;

    /**
     * Gets aliases.
     *
     * @return the aliases
     */
    public abstract List<String> getAliases();

    /**
     * Gets command example.
     *
     * @return the command example
     */
    public abstract String getCommandExample();

    /**
     * Gets fancy message.
     *
     * @return the fancy message
     */
    public abstract FancyMessage getFancyMessage();

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public abstract String getPermission();

    /**
     * Gets tabs.
     *
     * @param args the args
     * @return the tabs
     */
    public abstract List<String> getTabs(String[] args);

    /**
     * Has permission boolean.
     *
     * @param sender the sender
     * @return the boolean
     */
    public final boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

}
