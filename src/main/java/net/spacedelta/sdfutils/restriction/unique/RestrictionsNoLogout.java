package net.spacedelta.sdfutils.restriction.unique;

import net.spacedelta.sdfutils.restriction.model.IUniqueRestriction;

/**
 * @author Ellie :: 27/09/2019
 */
public class RestrictionsNoLogout implements IUniqueRestriction {

    // Before send to spawn...
    private int logoutGracePeriod;

    /**
     * The {@link net.spacedelta.sdfutils.restriction.Restriction} specialization for the restriction of logout timings.
     * This restriction will send the player back to spawn if they overstay the logoutGracePeriod after logging out.
     */
    public RestrictionsNoLogout() {
    }

    /**
     * The grace period the player has before they are sent back to spawn on their next logon
     * if the conditions are met.
     *
     * @return the grace period.
     */
    public int getLogoutGracePeriod() {
        return logoutGracePeriod;
    }

    /**
     * The new grace period the player has before they could be sent back to spawn on next logon.
     *
     * @param logoutGracePeriod the new grace period.
     */
    public void setLogoutGracePeriod(int logoutGracePeriod) {
        this.logoutGracePeriod = logoutGracePeriod;
    }

}
