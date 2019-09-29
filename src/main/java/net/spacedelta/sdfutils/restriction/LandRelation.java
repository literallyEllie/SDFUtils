package net.spacedelta.sdfutils.restriction;

import com.massivecraft.factions.struct.Relation;

/**
 * @author Ellie :: 29/09/2019
 */
public enum LandRelation {

    MEMBER(Relation.MEMBER),
    ALLY(Relation.ALLY),
    TRUCE(Relation.TRUCE),
    NEUTRAL(Relation.NEUTRAL),
    ENEMY(Relation.ENEMY),

    WILDERNESS(null),
    WARZONE(null),
    SAFEZONE(null);

    private Relation factionRelation;

    /**
     * A more sophisticated version of {@link Relation} allowing for relations with the system factions
     * like Wilderness, warzone and Safezone. This allows specific restrictions.
     *
     * @param relation the native relation type, may be null.
     */
    LandRelation(Relation relation) {
        this.factionRelation = relation;
    }

    /**
     * Check if the relation type is native to the Faction plugin.
     * For relations with System factions, it will not be.
     *
     * @return if the linked faction relation is not null.
     */
    public boolean isNativeFactionRelation() {
        return factionRelation != null;
    }

    /**
     * Gets the native relation type of {@link Relation} linked with selected enum value.
     * This will not apply to System factions.
     *
     * @return the linked relation type.
     * It may be null if it is not native.
     */
    public Relation getNativeFactionRelation() {
        return factionRelation;
    }

    /**
     * Shorthand checker to see if the selected relation is wilderness.
     *
     * @return if the selected relation is wilderness.
     */
    public boolean isWilderness() {
        return this == WILDERNESS;
    }

    /**
     * Shorthand checker to see if the selected relation is warzone.
     *
     * @return if the selected relation is warzone.
     */
    public boolean isWarZone() {
        return this == WARZONE;
    }

    /**
     * Shorthand checker to see if the selected relation is safezone.
     *
     * @return if the selected relation is safezone.
     */
    public boolean isSafeZone() {
        return this == SAFEZONE;
    }

}
