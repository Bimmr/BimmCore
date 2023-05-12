package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.commands.helpers.TabCompletionHelper;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command handler.
 */
public class CommandHandler implements TabCompleter, CommandExecutor {

    private ArrayList<SubCommand> commands;
    private JavaPlugin            plugin;
    private String                command;
    private FancyMessage          information;
    private String                unknownCommand;

    /**
     * Instantiates a new Command handler.
     *
     * @param plugin         the plugin
     * @param command        the command
     * @param information    the information
     * @param unknownCommand the unknown command
     */
    public CommandHandler(JavaPlugin plugin, String command, FancyMessage information, String unknownCommand) {
        this.commands = new ArrayList<SubCommand>();
        this.plugin = plugin;
        this.command = command;
        this.information = information;
        this.unknownCommand = unknownCommand;

        plugin.getCommand(command).setExecutor(this);

        plugin.getCommand(command).setTabCompleter(this);
    }

    /**
     * Gets subcommand.
     *
     * @param command the command
     * @return the subcommand
     */
    public SubCommand getSubcommand(String command) {
        for (SubCommand sc : commands)
            if (sc.getName().equalsIgnoreCase(command)) return sc;
        return null;
    }

    /**
     * Gets sub command.
     *
     * @param cmdName the cmd name
     * @return the sub command
     */
    public SubCommand getSubCommand(String cmdName) {
        for (SubCommand cmd : this.commands)
            if (cmd.getName().equalsIgnoreCase(cmdName)) return cmd;
        return null;
    }

    /**
     * Gets sub commands.
     *
     * @return the sub commands
     */
    public ArrayList<SubCommand> getSubCommands() {
        return this.commands;
    }

    /**
     * Is sub command boolean.
     *
     * @param command the command
     * @return the boolean
     */
    public boolean isSubCommand(String command) {
        return getSubCommand(command) != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase(command)) {
            Player p = null;
            if (sender instanceof Player) p = (Player) sender;

            if (args.length == 0) {
                if (p != null)
                    information.send(p);
                else
                    information.sendToConsole(sender);
            } else {
                for (SubCommand subCommand : this.commands)
                    if (subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toLowerCase()))) {
                        subCommand.execute(sender, args);
                        return true;
                    }
                sender.sendMessage(unknownCommand);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

        if (args.length == 1) {
            List<String> cmds = new ArrayList<String>();

            for (SubCommand subCommand : this.commands)
                cmds.add(subCommand.getName());

            return TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, cmds);
        } else {
            List<String> params = new ArrayList<String>();
            for (SubCommand subCommand : this.commands)
                if (subCommand.getName().equalsIgnoreCase(args[0]) || (subCommand.getAliases() != null && subCommand.getAliases().contains(args[0].toLowerCase())))
                    params = subCommand.getTabs(args);
            if (params != null && !params.isEmpty())
                return TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, params);
            else
                return TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, TabCompletionHelper.getOnlinePlayers());
        }
    }

    /**
     * Register sub command.
     *
     * @param subCommand the sub command
     */
    public void registerSubCommand(SubCommand subCommand) {
        if (!isSubCommand(subCommand.getName())) this.commands.add(subCommand);
    }

    /**
     * Un register sub command.
     *
     * @param subCommand the sub command
     */
    public void unRegisterSubCommand(SubCommand subCommand) {
        this.commands.remove(subCommand);
    }
}
