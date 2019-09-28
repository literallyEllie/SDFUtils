package net.spacedelta.sdfutils.restriction.model;

/**
 * @author Ellie :: 27/09/2019
 */
public enum RestrictionType {

    TP("noTP"), HOME("noHome"), LOGOUT("noLogout");

    private String configKey;

    /**
     * All the types of restrictions which the plugin supports.
     *
     * @param configKey the configuration section key which is in the config.
     */
    RestrictionType(String configKey) {
        this.configKey = configKey;
    }

    /**
     * Since the configuration keys are not formatted in the way as enums,
     * this method allows to get it from there using the {@link #getConfigKey()} value.
     *
     * @param configKey what shows in the config.
     * @return the restriction type which is mapped to that key or null.
     */
    public static RestrictionType fromKey(String configKey) {
        for (RestrictionType value : RestrictionType.values()) {
            if (value.configKey.equalsIgnoreCase(configKey))
                return value;
        }
        return null;
    }

    /**
     * Gets the configuration section key of the restriction type.
     *
     * @return the key.
     */
    public String getConfigKey() {
        return configKey;
    }

}
