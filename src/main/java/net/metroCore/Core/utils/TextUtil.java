package net.metroCore.Core.utils;

public class TextUtil {

    public static boolean isNumeric(String input) {
        if (input == null) return false;
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
