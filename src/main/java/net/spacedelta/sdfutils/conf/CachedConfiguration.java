package net.spacedelta.sdfutils.conf;

import com.google.common.collect.Maps;
import net.spacedelta.sdfutils.restriction.Restriction;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;

import java.util.Map;

/**
 * @author Ellie :: 27/09/2019
 */
public class CachedConfiguration {

    private Map<RestrictionType, Restriction> restrictions;

    /**
     * A cached configuration which stores all the data inside the config.yml
     * in an easy and accessible format.
     */
    public CachedConfiguration() {
        this.restrictions = Maps.newHashMap();
    }

    /**
     * A map of all the restrictions that have been loaded by the restriction type for convenience.
     *
     * @return a map of the loaded restrictions.
     */
    public Map<RestrictionType, Restriction> getRestrictions() {
        return restrictions;
    }

    /**
     * Set a new restriction data map.
     *
     * @param restrictions the new restrictions map.
     */
    public void setRestrictions(Map<RestrictionType, Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * A quick getter to get a restriction by its type.
     *
     * @param restrictionType the restriction type.
     * @return the restriction mapped to that type. Shouldn't be null.
     */
    public Restriction getRestriction(RestrictionType restrictionType) {
        return this.restrictions.get(restrictionType);
    }

}
