package net.spacedelta.sdfutils.hook;

import com.earth2me.essentials.Essentials;
import com.google.common.collect.Lists;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class EssentialsUtil {

    private Essentials essentials;

    public EssentialsUtil(Essentials essentials) {
        this.essentials = essentials;
    }

    public IUser getEssPlayer(Player player) {
        return essentials.getUser(player);
    }

    public List<Location> getHomesOf(Player player) {
        final IUser essPlayer = getEssPlayer(player);

        List<Location> homes = Lists.newArrayList();

        for (String homeName : essPlayer.getHomes()) {
            try {
                homes.add(essPlayer.getHome(homeName));
            } catch (Exception e) {
                // shouldn't even be called.
                e.printStackTrace();
            }
        }

        return homes;
    }

}
