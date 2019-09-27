package net.spacedelta.sdfutils.conf;

import com.google.common.collect.Lists;
import com.massivecraft.factions.struct.Relation;
import net.spacedelta.sdfutils.SDFUtils;
import net.spacedelta.sdfutils.restriction.Restriction;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class ConfigHandle {

    private final SDFUtils sdfUtils;

    public ConfigHandle(SDFUtils sdfUtils) {
        this.sdfUtils = sdfUtils;
    }

    public CachedConfiguration read() {
        final FileConfiguration config = sdfUtils.getConfig();

        CachedConfiguration cachedConfiguration = new CachedConfiguration();

        // Settings
        for (String settings : config.getConfigurationSection("settings").getKeys(false)) {
            // a spawn maybe.
        }

        // Restrictions
        for (String rConfKey : config.getConfigurationSection("restrictions").getKeys(false)) {
            RestrictionType restrictionType = RestrictionType.fromKey(rConfKey);
            if (restrictionType == null) {
                sdfUtils.logError("Unknown restriction type value at '" + rConfKey + " whilst reading config.");
                continue;
            }

            List<Relation> affectedRelations = Lists.newArrayList();
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

            switch (restrictionType) {
                case TP:
                    break;
                case HOME:
                    restriction.getRestrictionNoHome().setDeleteViolatingHomes(config.getBoolean("restrictions." + rConfKey + ".unique.deleteViolatingHomes"));
                    restriction.getRestrictionNoHome().setBlockedCommands(config.getStringList("restrictions." + rConfKey + ".unique.blockedCommands"));
                    break;
                case LOGOUT:
                    restriction.getRestrictionNoLogout().setLogoutGracePeriod(config.getInt("restrictions." + rConfKey + ".unique.logoutGracePeriod"));
            }

            cachedConfiguration.getRestrictions().put(restrictionType, restriction);
        }

        return cachedConfiguration;
    }

    public void reload() {
        // TODO
    }

    private List<Relation> parseRelationList(List<String> relationList) throws
            IllegalArgumentException {
        List<Relation> relations = Lists.newArrayList();
        relationList.forEach(s -> relations.add(Relation.valueOf(s.toUpperCase())));
        return relations;
    }

    private String setChatColored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
