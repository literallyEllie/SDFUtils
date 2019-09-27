package net.spacedelta.sdfutils;

import com.earth2me.essentials.Essentials;
import net.spacedelta.sdfutils.conf.CachedConfiguration;
import net.spacedelta.sdfutils.conf.ConfigHandle;
import net.spacedelta.sdfutils.hook.EssentialsUtil;
import net.spacedelta.sdfutils.hook.FactionsUtil;
import net.spacedelta.sdfutils.restriction.RestrictionListener;
import net.spacedelta.sdfutils.util.UtilTime;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ellie :: 27/09/2019
 */
public class SDFUtils extends JavaPlugin {

    private final String PERMISSION_CMD_SDFUTIL = "sdfutil.reload";

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

        logInfo("SDFUtils is being disabled.");
    }

    private void registerSoftDependencies() {

        final Plugin essentials = getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            logWarn("Could not find Essentials plugin, cannot hook into for spawn and homes!");
        } else essentialsUtils = new EssentialsUtil((Essentials) essentials);

    }

    private void registerCommands() {
        getCommand("sdfutil").setExecutor((commandSender, command, label, args) -> {

            if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
                commandSender.sendMessage("test");
                return true;
            }

            commandSender.sendMessage("no");

            return true;
        });
    }

    private void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }


    public void logInfo(String info) {
        getLogger().info(info);
    }

    public void logWarn(String warn) {
        getLogger().warning(warn);
    }

    public void logError(String error) {
        getLogger().severe(error);
    }



    // native plugin getters.

    public ConfigHandle getConfigHandle() {
        return configHandle;
    }

    public CachedConfiguration getCachedConfiguration() {
        return cachedConfiguration;
    }

    public RestrictionListener getRestrictionListener() {
        return restrictionListener;
    }

    public FactionsUtil getFactionsUtil() {
        return factionsUtil;
    }

    public EssentialsUtil getEssentialsUtils() {
        return essentialsUtils;
    }
}
