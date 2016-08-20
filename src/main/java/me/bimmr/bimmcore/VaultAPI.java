package me.bimmr.bimmcore;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


/**
 * An easy way to handle vault api
 */
public class VaultAPI {

    /**
     * @param use
     * the hasVault to set
     */

    private static Economy    economy;
    private static Permission permission;

    public static Economy getEconomy() {
        if (economy == null)
            setupEconomy();
        return economy;
    }

    public static Permission getPermission() {
        if (permission == null)
            setupPermission();
        return permission;
    }

    /**
     * Does the server have vault and did everything for vault show okay
     *
     * @return true/false
     */
    public static boolean hasVaultOnServer() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;

    }

    /**
     * Load Permissions
     *
     * @return
     */
    private static boolean setupPermission() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    /**
     * Load Economy
     *
     * @return if it loaded
     */
    private static boolean setupEconomy() {
        try {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null)
                economy = economyProvider.getProvider();
        } catch (Exception e) {
        }
        return (economy != null);
    }

}
