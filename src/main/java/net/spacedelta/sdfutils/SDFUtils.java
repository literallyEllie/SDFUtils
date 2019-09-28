package net.spacedelta.sdfutils;

import com.earth2me.essentials.Essentials;
import net.spacedelta.sdfutils.conf.CachedConfiguration;
import net.spacedelta.sdfutils.conf.ConfigHandle;
import net.spacedelta.sdfutils.hook.EssentialsUtil;
import net.spacedelta.sdfutils.hook.FactionsUtil;
import net.spacedelta.sdfutils.restriction.RestrictionListener;
import net.spacedelta.sdfutils.util.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ellie :: 27/09/2019
 */
public class SDFUtils extends JavaPlugin {

    private ConfigHandle configHandle;
    private CachedConfiguration cachedConfiguration;

    private RestrictionListener restrictionListener;

    private FactionsUtil factionsUtil;
    private EssentialsUtil essentialsUtils;

    @Override
    public void onEnable() {
        long start = UtilTime.now();

        // Load config.
        this.configHandle = new ConfigHandle(this);
        this.cachedConfiguration = configHandle.read();

        this.restrictionListener = new RestrictionListener(this);
        registerListener(restrictionListener);

        // Ain't about that static life.
        this.factionsUtil = new FactionsUtil();
        registerSoftDependencies();

        registerCommands();

        logInfo("SDFUtils startup completed in " + (UtilTime.now() - start) + "ms.");
    }

    @Override
    public void onDisable() {

        setCachedConfiguration(null);

        logInfo("SDFUtils is being disabled.");
    }

    /**
     * Log info to console.
     *
     * @param info the info to log.
     */
    public void logInfo(String info) {
        getLogger().info(info);
    }

    /**
     * Log a warning to console.
     *
     * @param warn the warning to log.
     */
    public void logWarn(String warn) {
        getLogger().warning(warn);
    }

    /**
     * Log an error to console.
     *
     * @param error the the error to log.
     */
    public void logError(String error) {
        getLogger().severe(error);
    }

    /**
     * Registers an array of listeners.
     *
     * @param listeners the listeners to register.
     */
    private void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    /**
     * Checks the presence of all soft dependencies and if applicable stores an instance of them.
     */
    private void registerSoftDependencies() {

        final Plugin essentials = getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            logWarn("Could not find Essentials plugin, cannot hook into for spawn and homes!");
        } else essentialsUtils = new EssentialsUtil((Essentials) essentials);

    }

    /**
     * Registers the commands for the plugin.
     * Since this command is very small and also the only command, it is just here.
     */
    private void registerCommands() {
        getCommand("sdfutil").setExecutor((commandSender, command, label, args) -> {

            if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /sdfutil reload " + ChatColor.RESET + "(sdfutil.reload)");
                return true;
            }

            configHandle.reload();
            commandSender.sendMessage(ChatColor.GREEN + "Plugin reloaded.");
            return true;
        });
    }

    // native plugin getters.

    /**
     * Gives an instance of the config handle which manages the config I/O.
     *
     * @return the config handle.
     */
    public ConfigHandle getConfigHandle() {
        return configHandle;
    }

    /**
     * Gives an instance of the currently cached configuration.
     * This is a serialized format of the config.yml.
     *
     * @return the cached configuration.
     */
    public CachedConfiguration getCachedConfiguration() {
        return cachedConfiguration;
    }

    /**
     * Set the cached configuration to a new value, may be null.
     *
     * @param cachedConfiguration the new/updated cached configuration.
     */
    public void setCachedConfiguration(CachedConfiguration cachedConfiguration) {
        this.cachedConfiguration = cachedConfiguration;
    }

    /**
     * Gets the restriction listener which handles all the restriction dealings.
     *
     * @return the restriction listener.
     */
    public RestrictionListener getRestrictionListener() {
        return restrictionListener;
    }

    /**
     * A hook class to hook into SavageFactions/Factions to ease getting data.
     *
     * @return the Factions util hook.
     */
    public FactionsUtil getFactionsUtil() {
        return factionsUtil;
    }

    /**
     * A hook class to hook into Essentials to ease getting data.
     *
     * @return the Essentials util hook.
     */
    public EssentialsUtil getEssentialsUtils() {
        return essentialsUtils;
    }

}
