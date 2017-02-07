package me.bimmr.bimmcore.commands;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to help with the TabCompletion for Bukkit.
 * <p>
 * Can be used by anybody, as long as you provide credit for it.
 *
 * @author D4rKDeagle
 */
public class TabCompletionHelper {

    public static String[] getOnlinePlayerNames() {
        Player[] onlinePlayers = BimmCore.getOnlinePlayers();
        String[] onlinePlayerNames = new String[onlinePlayers.length];

        for (int i = 0; i < onlinePlayers.length; i++)
            onlinePlayerNames[i] = onlinePlayers[i].getName();

        return onlinePlayerNames;
    }

    public static List<String> getOnlinePlayers() {
        Player[] onlinePlayers = BimmCore.getOnlinePlayers();
        List<String> onlinePlayerNames = new ArrayList<String>();

        for (Player player : onlinePlayers)
            onlinePlayerNames.add(player.getName());
        return onlinePlayerNames;
    }

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
