package net.spacedelta.sdfutils.hook;

import com.earth2me.essentials.Essentials;
import com.google.common.collect.Maps;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Ellie :: 27/09/2019
 */
public class EssentialsUtil {

    private Essentials essentials;

    /**
     * A hook to easily access the {@link Essentials} data.
     *
     * @param essentials an instance of the Essentials plugin.
     */
    public EssentialsUtil(Essentials essentials) {
        this.essentials = essentials;
    }

    /**
     * Get an {@link IUser} instance of a player.
     *
     * @param player the player to get the instance of.
     * @return a {@link IUser} instance of the player.
     */
    public IUser getEssPlayer(Player player) {
        return essentials.getUser(player);
    }

    /**
     * A map of the homes of a player mapped to its respective name.
     *
     * @param player the player to get the homes of.
     * @return a map of the home names and their locations.
     */
    public Map<String, Location> getHomesOf(Player player) {
        final IUser essPlayer = getEssPlayer(player);

        Map<String, Location> homes = Maps.newHashMap();

        for (String homeName : essPlayer.getHomes()) {
            try {
                homes.put(homeName, essPlayer.getHome(homeName));
            } catch (Exception e) {
                // shouldn't even be called.
                e.printStackTrace();
            }
        }

        return homes;
    }

}
