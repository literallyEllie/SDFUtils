package net.spacedelta.sdfutils.restriction.unique;

import net.spacedelta.sdfutils.restriction.model.UniqueRestriction;

/**
 * @author Ellie :: 27/09/2019
 */
public class RestrictionsNoLogout implements UniqueRestriction {

    // Before send to spawn...
    private int logoutGracePeriod;

    public RestrictionsNoLogout() {
    }

    public int getLogoutGracePeriod() {
        return logoutGracePeriod;
    }

    public void setLogoutGracePeriod(int logoutGracePeriod) {
        this.logoutGracePeriod = logoutGracePeriod;
    }

}
