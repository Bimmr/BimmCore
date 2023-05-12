package me.bimmr.bimmcore.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


/**
 * The type Vault util.
 */
public class VaultUtil {

    private static Economy    economy;
    private static Permission permission;

    /**
     * Gets economy.
     *
     * @return the economy
     */
    public static Economy getEconomy() {
        if (economy == null)
            setupEconomy();
        return economy;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public static Permission getPermission() {
        if (permission == null)
            setupPermission();
        return permission;
    }

    /**
     * Has vault on server boolean.
     *
     * @return the boolean
     */
    public static boolean hasVaultOnServer() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;

    }

    private static boolean setupPermission() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

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
