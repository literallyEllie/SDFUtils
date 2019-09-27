package net.spacedelta.sdfutils.conf;

import com.google.common.collect.Maps;
import net.spacedelta.sdfutils.restriction.Restriction;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import org.bukkit.Location;

import java.util.Map;

/**
 * @author Ellie :: 27/09/2019
 */
public class CachedConfiguration {

    private Location spawn;

    private Map<RestrictionType, Restriction> restrictions;

    public CachedConfiguration() {
        this.restrictions = Maps.newHashMap();
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Map<RestrictionType, Restriction> getRestrictions() {
        return restrictions;
    }

    public Restriction getRestriction(RestrictionType restrictionType) {
        return this.restrictions.get(restrictionType);
    }

    public void setRestrictions(Map<RestrictionType, Restriction> restrictions) {
        this.restrictions = restrictions;
    }
}
