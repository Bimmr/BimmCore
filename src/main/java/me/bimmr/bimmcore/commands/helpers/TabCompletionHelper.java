package me.bimmr.bimmcore.commands.helpers;

import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tab completion helper.
 */
public class TabCompletionHelper {

    /**
     * Get online player names string [ ].
     *
     * @return the string [ ]
     */
    public static String[] getOnlinePlayerNames() {
        Player[] onlinePlayers = Reflection.getOnlinePlayers();
        String[] onlinePlayerNames = new String[onlinePlayers.length];

        for (int i = 0; i < onlinePlayers.length; i++)
            onlinePlayerNames[i] = onlinePlayers[i].getName();

        return onlinePlayerNames;
    }

    /**
     * Gets online players.
     *
     * @return the online players
     */
    public static List<String> getOnlinePlayers() {
        Player[] onlinePlayers = Reflection.getOnlinePlayers();
        List<String> onlinePlayerNames = new ArrayList<String>();

        for (Player player : onlinePlayers)
            onlinePlayerNames.add(player.getName());
        return onlinePlayerNames;
    }

    /**
     * Gets possible completions for given args.
     *
     * @param args                      the args
     * @param possibilitiesOfCompletion the possibilities of completion
     * @return the possible completions for given args
     */
    public static List<String> getPossibleCompletionsForGivenArgs(String[] args, List<String> possibilitiesOfCompletion) {
        String argumentToFindCompletionFor = args[args.length - 1];

        List<String> listOfPossibleCompletions = new ArrayList<String>();

        for (int i = 0; i < possibilitiesOfCompletion.size(); ++i) {
            String foundString = possibilitiesOfCompletion.get(i);

            if (foundString.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length()))
                listOfPossibleCompletions.add(foundString);
        }

        return listOfPossibleCompletions;
    }
}
