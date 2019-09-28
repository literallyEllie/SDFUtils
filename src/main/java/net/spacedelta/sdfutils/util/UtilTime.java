package net.spacedelta.sdfutils.util;

/**
 * @author Ellie :: 27/09/2019
 */
public class UtilTime {

    /**
     * A quick method to get the current time in millis.
     *
     * @return the current value of {@link System#currentTimeMillis()}.
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * A method to check if an amount of time in millis has elapsed from a past timestamp.
     * @param time the past timestamp from {@link System#currentTimeMillis()}.
     * @param required the required time in millis that must have passed.
     * @return if the current time minus the past timestamp is greater than the required.
     *
     *  If it is, the time has thus elapsed.
     */
    public static boolean hasElapsed(long time, long required) {
        return now() - time > required;
    }

}
