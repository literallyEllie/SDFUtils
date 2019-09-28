package net.spacedelta.sdfutils.restriction;

import com.massivecraft.factions.struct.Relation;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import net.spacedelta.sdfutils.restriction.model.IUniqueRestriction;
import net.spacedelta.sdfutils.restriction.unique.RestrictionsNoHome;
import net.spacedelta.sdfutils.restriction.unique.RestrictionsNoLogout;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class Restriction {

    private final RestrictionType restrictionType;

    private boolean enabled;
    private String bypassPermission;
    private List<Relation> affectedRelations;
    private String denyMessage;

    // Special
    private IUniqueRestriction uniqueRestriction;

    /**
     * A generic restriction which denies certain things from happening in certain regions.
     *
     * @param restrictionType the enum value of restriction type.
     * @param enabled is the restriction enabled.
     * @param bypassPermission the permission to bypass this restriction.
     * @param affectedRelations a list of faction relations which this restriction applies to.
     * @param denyMessage the message to send to a player when the restriction is triggered.
     */
    public Restriction(RestrictionType restrictionType, boolean enabled, String bypassPermission, List<Relation> affectedRelations, String denyMessage) {
        this.restrictionType = restrictionType;
        this.enabled = enabled;
        this.bypassPermission = bypassPermission;
        this.affectedRelations = affectedRelations;
        this.denyMessage = denyMessage;

        // Specializations.
        switch (restrictionType) {
            case TP:
                break;
            case HOME:
                uniqueRestriction = new RestrictionsNoHome();
                break;
            case LOGOUT:
                uniqueRestriction = new RestrictionsNoLogout();
                break;
        }

    }

    /**
     * Gets the restriction type of this restriction.
     *
     * @return the restriction type.
     */
    public RestrictionType getRestrictionType() {
        return restrictionType;
    }

    /**
     * Gets if the restriction is enabled or not, should checks be done.
     *
     * @return is the restriction enabled or not.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set if the restriction is enabled or not.
     *
     * @param enabled the new enabled value.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the permission to bypass this restriction.
     *
     * @return the permission.
     */
    public String getBypassPermission() {
        return bypassPermission;
    }

    /**
     * Sets a new bypass permission.
     *
     * @param bypassPermission the new bypass permission.
     */
    public void setBypassPermission(String bypassPermission) {
        this.bypassPermission = bypassPermission;
    }

    /**
     * Gets the affected faction relations which this restriction covers.
     *
     * @return the list of affected relations.
     */
    public List<Relation> getAffectedRelations() {
        return affectedRelations;
    }

    /**
     * Sets a new list of affected relations which this restriction covers.
     *
     * @param affectedRelations the new list.
     */
    public void setAffectedRelations(List<Relation> affectedRelations) {
        this.affectedRelations = affectedRelations;
    }

    /**
     * Gets the message which is sent to the player when the restriction is denied.
     *
     * @return the deny message.
     */
    public String getDenyMessage() {
        return denyMessage;
    }

    /**
     * Sets a new deny message to send.
     *
     * @param denyMessage the new deny message.
     */
    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }

    /**
     * Gets the generic interface type of the restriction.
     * This only applies to some restrictions.
     *
     * @return the implementation of the unique restriction, may be null.
     */
    public IUniqueRestriction getUniqueRestriction() {
        return uniqueRestriction;
    }

    // may be null / error if not uncheched.

    /**
     * An unchecked cast of uniqueRestriction to {@link RestrictionsNoHome}.
     * Should only be called with certainty.
     * The instance of this, if at all, is set in the constructor.
     *
     * @return uniqueRestriction cast to {@link RestrictionsNoHome}.
     */
    public RestrictionsNoHome getRestrictionNoHome() {
        return (RestrictionsNoHome) uniqueRestriction;
    }

    /**
     * An unchecked cast of uniqueRestriction to {@link RestrictionsNoLogout}.
     * Should only be called with certainty.
     * The instance of this, if at all, is set in the constructor.
     *
     * @return uniqueRestriction cast to {@link RestrictionsNoLogout}.
     */
    public RestrictionsNoLogout getRestrictionNoLogout() {
        return (RestrictionsNoLogout) uniqueRestriction;
    }

}
