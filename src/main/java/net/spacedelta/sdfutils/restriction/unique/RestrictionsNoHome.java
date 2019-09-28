package net.spacedelta.sdfutils.restriction.unique;

import net.spacedelta.sdfutils.restriction.model.IUniqueRestriction;

import java.util.List;

/**
 * @author Ellie :: 27/09/2019
 */
public class RestrictionsNoHome implements IUniqueRestriction {

    private boolean deleteViolatingHomes;
    private String deletedViolatedHomesMessage;
    private List<String> blockedCommands;

    /**
     * The {@link net.spacedelta.sdfutils.restriction.Restriction} specialization for the restriction of no home.
     * A restriction to stop certain commands from being executed in a region
     * and deleting homes which are where they shouldn't be.
     */
    public RestrictionsNoHome() {
    }

    /**
     * The value of if homes are being deleted for being in a location which they shouldn't be.
     *
     * @return if violating homes should be deleted.
     */
    public boolean isDeleteViolatingHomes() {
        return deleteViolatingHomes;
    }

    /**
     * Set if violated homes should be deleted or not for being in a location where they shouldn't be.
     *
     * @param deleteViolatingHomes the new setting.
     */
    public void setDeleteViolatingHomes(boolean deleteViolatingHomes) {
        this.deleteViolatingHomes = deleteViolatingHomes;
    }

    /**
     * Gets the message to send to the user when their homes are deleted.
     *
     * @return the message.
     */
    public String getDeletedViolatedHomesMessage() {
        return deletedViolatedHomesMessage;
    }

    /**
     * Sets the new message to send to users when their homes are deleted.
     *
     * @param deletedViolatedHomesMessage the new message.
     */
    public void setDeletedViolatedHomesMessage(String deletedViolatedHomesMessage) {
        this.deletedViolatedHomesMessage = deletedViolatedHomesMessage;
    }

    /**
     * Gets a String list of commands which are blocked in a certain region.
     *
     * @return the String list of commands blocked.
     */
    public List<String> getBlockedCommands() {
        return blockedCommands;
    }

    /**
     * Sets a new list of the commands that should be blocked in a certain region.
     *
     * @param blockedCommands the new list of blocked commands.
     */
    public void setBlockedCommands(List<String> blockedCommands) {
        this.blockedCommands = blockedCommands;
    }

}
