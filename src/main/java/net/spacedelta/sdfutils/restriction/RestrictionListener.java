package net.spacedelta.sdfutils.restriction;

import com.google.common.collect.Maps;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;
import net.spacedelta.sdfutils.SDFUtils;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import net.spacedelta.sdfutils.util.UtilTime;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

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
            commandArgOne = commandArgOne.split(":")[1];
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
    public void on(final PlayerLoginEvent e) {
        final Restriction restriction = sdfUtils.getCachedConfiguration().getRestriction(RestrictionType.LOGOUT);
        if (!restriction.isEnabled()) return;

        final Player player = e.getPlayer();

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

    // TODO maybe look to simplify.
    private boolean violatesRestriction(Player player, List<Relation> affectedRelations, Location customLocation) {
        final Faction interferingFaction = sdfUtils.getFactionsUtil().getFactionAt(customLocation == null ? player.getLocation() : customLocation);
        if (interferingFaction == null) return false;

        final FPlayer factionPlayer = sdfUtils.getFactionsUtil().getPlayer(player);
        // If player doesn't have faction, presume they are truce.
        final Relation playerRelationToInterfering = factionPlayer.hasFaction() ? factionPlayer.getFaction().getRelationTo(interferingFaction) : Relation.TRUCE;

        return affectedRelations.contains(playerRelationToInterfering);
    }

}
