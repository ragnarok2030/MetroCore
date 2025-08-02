package net.metroCore.Core.utils;

public class ColorUtil {

    public static String color(String input) {
        return input.replace("&", "§");
    }

    public static String strip(String input) {
        return input.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
    }
}
