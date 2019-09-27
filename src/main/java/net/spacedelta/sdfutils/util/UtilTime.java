package net.spacedelta.sdfutils.util;

/**
 * @author Ellie :: 27/09/2019
 */
public class UtilTime {

    public static boolean hasElapsed(long time, long required) {
        return now() - time > required;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

}
