package net.spacedelta.sdfutils.restriction;

import com.massivecraft.factions.struct.Relation;
import net.spacedelta.sdfutils.restriction.model.RestrictionType;
import net.spacedelta.sdfutils.restriction.model.UniqueRestriction;
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
    private UniqueRestriction uniqueRestriction;

    public Restriction(RestrictionType restrictionType, boolean enabled, String bypassPermission, List<Relation> affectedRelations, String denyMessage) {
        this.restrictionType = restrictionType;
        this.enabled = enabled;
        this.bypassPermission = bypassPermission;
        this.affectedRelations = affectedRelations;
        this.denyMessage = denyMessage;

        //
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

    public RestrictionType getRestrictionType() {
        return restrictionType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

    public void setBypassPermission(String bypassPermission) {
        this.bypassPermission = bypassPermission;
    }

    public List<Relation> getAffectedRelations() {
        return affectedRelations;
    }

    public void setAffectedRelations(List<Relation> affectedRelations) {
        this.affectedRelations = affectedRelations;
    }

    public String getDenyMessage() {
        return denyMessage;
    }

    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }

    public UniqueRestriction getUniqueRestriction() {
        return uniqueRestriction;
    }

    // may be null / error if not uncheched.

    public RestrictionsNoHome getRestrictionNoHome() {
        return (RestrictionsNoHome) uniqueRestriction;
    }

    public RestrictionsNoLogout getRestrictionNoLogout() {
        return (RestrictionsNoLogout) uniqueRestriction;
    }

}
