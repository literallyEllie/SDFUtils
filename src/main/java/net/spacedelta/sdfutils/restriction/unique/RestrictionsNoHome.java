package net.spacedelta.sdfutils.restriction.unique;

import net.spacedelta.sdfutils.restriction.model.UniqueRestriction;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class RestrictionsNoHome implements UniqueRestriction {

    private boolean deleteViolatingHomes;
    private List<String> blockedCommands;

    public RestrictionsNoHome() {
    }

    public boolean isDeleteViolatingHomes() {
        return deleteViolatingHomes;
    }

    public void setDeleteViolatingHomes(boolean deleteViolatingHomes) {
        this.deleteViolatingHomes = deleteViolatingHomes;
    }

    public List<String> getBlockedCommands() {
        return blockedCommands;
    }

    public void setBlockedCommands(List<String> blockedCommands) {
        this.blockedCommands = blockedCommands;
    }


}
