package net.spacedelta.sdfutils.restriction.model;

/**
 * @author Ellie :: 27/09/2019
 */
public enum RestrictionType {

    TP("noTP"), HOME("noHome"), LOGOUT("noLogout")
    ;

    public static RestrictionType fromKey(String configKey) {
        for (RestrictionType value : RestrictionType.values()) {
            if (value.configKey.equalsIgnoreCase(configKey))
                return value;
        }
        return null;
    }

    private String configKey;

    RestrictionType(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }

}
