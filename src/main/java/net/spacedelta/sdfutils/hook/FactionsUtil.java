package net.spacedelta.sdfutils.hook;

import com.massivecraft.factions.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Ellie :: 27/09/2019
 */
public class FactionsUtil {

    /**
     * Get a {@link FPlayer} instance of a player.
     *
     * @param player the player to get the instance of.
     * @return the {@link FPlayer} instance of the player.
     */
    public FPlayer getPlayer(Player player) {
        return FPlayers.getInstance().getByPlayer(player);
    }

    /**
     * Gets the faction at the specified location.
     *
     * @param location the location to get the faction at.
     * @return the faction at the location of this location, will never be null.
     */
    public Faction getFactionAt(Location location) {
        return Board.getInstance().getFactionAt(new FLocation(location));
    }

}
