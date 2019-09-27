package net.spacedelta.sdfutils.hook;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Ellie :: 27/09/2019
 */
public class FactionsUtil {

    public FPlayer getPlayer(Player player) {
        return FPlayers.getInstance().getByPlayer(player);
    }

    public Faction getFactionAt(Location location) {
        return Board.getInstance().getFactionAt(new FLocation(location));
    }

}
