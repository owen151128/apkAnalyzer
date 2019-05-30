package util;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-05-30 12:04
 * <p>
 * Providing features related to Console class
 */
public class Console {
    private static final String LINE = "================================================================================";

    /**
     * Write console
     *
     * @param message message
     */
    public static void write(String message) {
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Write console using format string
     *
     * @param formatString Target format string
     * @param args         Target arguments
     */
    public static void write(String formatString, Object... args) {
        System.out.println(String.format(formatString, args));
        System.out.println(LINE);
    }

    /**
     * Bold write console
     *
     * @param message message
     */
    public static void bold(String message) {
        System.out.println("####################  " + message + "  ####################");
    }
}
