package net.spacedelta.sdfutils.restriction;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;
import net.ess3.api.IUser;
import net.spacedelta.sdfutils.SDFUtils;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import net.spacedelta.sdfutils.util.D;
import net.spacedelta.sdfutils.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Ellie :: 27/09/2019
 */
public class RestrictionListener implements Listener {

    private SDFUtils sdfUtils;

    private Map<UUID, Long> logoutTimers;

    /**
     * The main doer-of-things in the plugins.
     * It listens for all the events and checks if the restrictions for said event is enabled and all of that.
     * It also manages logout timers and cleaning up Essentials homes.
     *
     * @param sdfUtils the plugin instance.
     */
    public RestrictionListener(SDFUtils sdfUtils) {
        this.sdfUtils = sdfUtils;
        this.logoutTimers = Maps.newHashMap();
    }

    /* No tp event blocker */

    @EventHandler
    public void on(final PlayerTeleportEvent e) {
        final Restriction restriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.TP);
        if (!restriction.isEnabled()) return;

        final Player player = e.getPlayer();
        if (player.hasPermission(restriction.getBypassPermission())) return;

        // ensure works as expected.
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {

            if (violatesRestriction(player, restriction.getAffectedRelations(), e.getTo())) {
                player.sendMessage(restriction.getDenyMessage());
                e.setCancelled(true);
            }

        }
    }

    /* Home command blocker. */

    @EventHandler
    public void on(final PlayerCommandPreprocessEvent e) {
        final Restriction restriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.HOME);
        if (!restriction.isEnabled()) return;

        final Player player = e.getPlayer();
        if (player.hasPermission(restriction.getBypassPermission())) return;

        String commandArgOne = e.getMessage().split(" ")[0].toLowerCase();
        final List<String> blockedCommands = restriction.getRestrictionNoHome().getBlockedCommands();

        if (commandArgOne.contains(":")) {
            final String[] split = commandArgOne.split(":", 2);

            if (split.length > 0) {
                commandArgOne = split[1];
            } else return;

        } else commandArgOne = commandArgOne.substring(1); // trim '/'

        String finalArg = commandArgOne;
        if (blockedCommands.stream().noneMatch(finalArg::equalsIgnoreCase)) return;

        // They are violating.
        if (violatesRestriction(player, restriction.getAffectedRelations(), null)) {
            player.sendMessage(restriction.getDenyMessage());
            e.setCancelled(true);
        }

    }

    /* No logout stuff */

    @EventHandler
    public void on(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        final Restriction homeRestriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.HOME);
        if (homeRestriction.isEnabled() && homeRestriction.getRestrictionNoHome().isDeleteViolatingHomes() && !player.hasPermission(homeRestriction.getBypassPermission())) {
            checkEssentialsHomesAndClean(player, homeRestriction, true);
        }

        final Restriction restriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.LOGOUT);
        if (!restriction.isEnabled()) return;

        if (logoutTimers.containsKey(player.getUniqueId())) {
            if (player.hasPermission(restriction.getBypassPermission())) {
                logoutTimers.remove(player.getUniqueId());
                return;
            }

            final Long logoutTime = logoutTimers.get(player.getUniqueId());

            if (UtilTime.hasElapsed(logoutTime, TimeUnit.SECONDS.toMillis(restriction.getRestrictionNoLogout().getLogoutGracePeriod()))) {
                // TP to spawn.
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage(restriction.getDenyMessage());
                logoutTimers.remove(player.getUniqueId());
                return;
            }

            logoutTimers.remove(player.getUniqueId());
        }

    }

    @EventHandler
    public void on(final PlayerQuitEvent e) {
        final Restriction restriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.LOGOUT);
        if (!restriction.isEnabled()) return;

        final Player player = e.getPlayer();
        if (player.hasPermission(restriction.getBypassPermission())) return;

        if (violatesRestriction(player, restriction.getAffectedRelations(), null)) {
            logoutTimers.put(player.getUniqueId(), UtilTime.now());
        }

    }

    /**
     * A method to cleanup and remove violating homes of a player.
     * Only use method for the {@link net.spacedelta.sdfutils.restriction.unique.RestrictionsNoHome} restriction.
     *
     * @param player the player to use.
     * @param restriction a {@link net.spacedelta.sdfutils.restriction.unique.RestrictionsNoHome} instance of a restriction.
     * @param verbose should output messages be sent the player.
     */
    public void checkEssentialsHomesAndClean(Player player, Restriction restriction, boolean verbose) {
        final Map<String, Location> playerHomes = sdfUtils.getEssentialsUtils().getHomesOf(player);

        List<String> violatingHomes = Lists.newArrayList();

        playerHomes.forEach((homeName, location) -> {
            if (violatesRestriction(player, restriction.getAffectedRelations(), location))
                violatingHomes.add(homeName);
        });

        if (!violatingHomes.isEmpty()) {
            final IUser essPlayer = sdfUtils.getEssentialsUtils().getEssPlayer(player);

            violatingHomes.forEach(homeName -> {
                try {
                    essPlayer.delHome(homeName);
                } catch (Exception e) {
                    // shouldn't call.
                    sdfUtils.logError("Failed to delete player home '" + homeName + "' of " + player.getName() + " when trying to delete bad homes");
                    e.printStackTrace();
                }
            });

            if (verbose) {
                player.sendMessage(
                        restriction.getRestrictionNoHome().getDeletedViolatedHomesMessage().replace("%deletedHomes%", Joiner.on(", ").join(violatingHomes)));
            }

        }
    }


    /**
     * A very core method to check if a player/location is violating a restriction space.
     * It will get the possible faction at the point of either the user or a block.
     * If that faction is null, it will return false.
     * If the player has no faction they will be presumed neutral to a player faction.
     * All the factions are iterated through and if one of the affected relations matches with the faction at the space
     * it will trigger.
     *
     * @param player the player to get the data of.
     * @param affectedRelations the list of affected relations to go by.
     * @param customLocation a custom position to use that has some relation to player
     * @return if the restriction is being triggered in the position of the player/customLocation.
     */
    private boolean violatesRestriction(Player player, List<LandRelation> affectedRelations, Location customLocation) {
        // TODO maybe look to simplify.
        final Faction interferingFaction = sdfUtils.getFactionsUtil().getFactionAt(customLocation == null ? player.getLocation() : customLocation);
        if (interferingFaction == null /*|| interferingFaction.isWilderness() || interferingFaction.isSafeZone()*/) {
            return false;
        }

        final FPlayer factionPlayer = sdfUtils.getFactionsUtil().getPlayer(player);
        // If player doesn't have faction, they are neutral.
        final Relation playerRelationToInterfering = factionPlayer.hasFaction() ? factionPlayer.getFaction().getRelationTo(interferingFaction) : Relation.NEUTRAL;

        for (LandRelation affectedRelation : affectedRelations) {

            // Server factions.
            if ((affectedRelation.isWilderness() && interferingFaction.isWilderness())
                    || (affectedRelation.isSafeZone() && interferingFaction.isSafeZone())
                    || (affectedRelation.isWarZone() && interferingFaction.isWarZone()))
                return true;

            // Check for player factions.
            if (interferingFaction.isNormal()
                    && affectedRelation.isNativeFactionRelation() && affectedRelation.getNativeFactionRelation() == playerRelationToInterfering)
                return true;

        }

        return false;
    }

}
