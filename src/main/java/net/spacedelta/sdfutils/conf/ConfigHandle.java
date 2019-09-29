package net.spacedelta.sdfutils.conf;

import com.google.common.collect.Lists;
import com.massivecraft.factions.struct.Relation;
import net.spacedelta.sdfutils.SDFUtils;
import net.spacedelta.sdfutils.restriction.LandRelation;
import net.spacedelta.sdfutils.restriction.Restriction;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class ConfigHandle {

    private final SDFUtils sdfUtils;

    /**
     * A config handler class to handle the reading of the config.
     * It can also reload and refresh the current {@link CachedConfiguration} stored in the {@link SDFUtils} class.
     *
     * @param sdfUtils an instance of the plugin.
     */
    public ConfigHandle(SDFUtils sdfUtils) {
        this.sdfUtils = sdfUtils;
        sdfUtils.saveDefaultConfig();
    }

    /**
     * Read from the cached {@link JavaPlugin#getConfig()} and stored into an instance of {@link CachedConfiguration} then returned.
     * Remember to reload by {@link JavaPlugin#reloadConfig()} or this will have no effect.
     *
     * @return the configuration mapped to an accessible object.
     */
    public CachedConfiguration read() {
        final FileConfiguration config = sdfUtils.getConfig();

        CachedConfiguration cachedConfiguration = new CachedConfiguration();

        // Restrictions
        for (String rConfKey : config.getConfigurationSection("restrictions").getKeys(false)) {
            RestrictionType restrictionType = RestrictionType.fromKey(rConfKey);
            if (restrictionType == null) {
                sdfUtils.logError("Unknown restriction type value at '" + rConfKey + " whilst reading config.");
                continue;
            }

            List<LandRelation> affectedRelations = Lists.newArrayList();
            try {
                affectedRelations = parseRelationList(config.getStringList("restrictions." + rConfKey + ".relations"));
            } catch (IllegalArgumentException e) {
                sdfUtils.logError("Failed to read affected regions for " + rConfKey + ": " + e.getMessage());
            }

            Restriction restriction = new Restriction(restrictionType,
                    config.getBoolean("restrictions." + rConfKey + ".enabled", true),
                    config.getString("restrictions." + rConfKey + ".bypassPerm", "spacedelta.staff"),
                    affectedRelations,
                    setChatColored(config.getString("restrictions." + rConfKey + ".denyMessage", "&cYou cannot do that here. (err)")));

            if (config.isConfigurationSection("restrictions." + rConfKey + ".unique")) {
                switch (restrictionType) {
                    case TP:
                        break;
                    case HOME:
                        restriction.getRestrictionNoHome().setDeleteViolatingHomes(config.getBoolean("restrictions." + rConfKey + ".unique.deleteViolatingHomes"));
                        restriction.getRestrictionNoHome().setDeletedViolatedHomesMessage(
                                setChatColored(config.getString("restrictions." + rConfKey + ".unique.deletedViolatedHomesMessage")));
                        restriction.getRestrictionNoHome().setBlockedCommands(config.getStringList("restrictions." + rConfKey + ".unique.blockedCommands"));
                        break;
                    case LOGOUT:
                        restriction.getRestrictionNoLogout().setLogoutGracePeriod(config.getInt("restrictions." + rConfKey + ".unique.logoutGracePeriod"));
                }
            }

            cachedConfiguration.getRestrictions().put(restrictionType, restriction);
        }

        return cachedConfiguration;
    }

    /**
     * Reloads the default storage config and sets the new stored instance into {@link SDFUtils}
     */
    public void reload() {
        sdfUtils.reloadConfig();
        sdfUtils.setCachedConfiguration(read());
    }

    /**
     * Parses a list of {@link Relation} from a string list.
     *
     * @param relationList the list of raw relations.
     * @return a list of the parsed relations.
     * @throws IllegalArgumentException when one of the values does not map to a {@link Relation} enum value.
     */
    private List<LandRelation> parseRelationList(List<String> relationList) throws
            IllegalArgumentException {
        List<LandRelation> relations = Lists.newArrayList();
        relationList.forEach(s -> relations.add(LandRelation.valueOf(s.toUpperCase())));
        return relations;
    }

    /**
     * Color a chat message using '&' symbols.
     *
     * @param message the message to color.
     * @return a colored message.
     */
    private String setChatColored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
