package net.metroCore.Core.utils;

import org.bukkit.command.CommandSender;

public final class ChatUtil {
    private ChatUtil() {}

    /** Center a message in chat by character count (assumes fixed-width font). */
    public static void sendCentered(CommandSender to, String message) {
        // Minecraft chat is ~40 characters wide
        int maxChars = 40;
        int msgLength = stripColor(message).length();
        int spaces = (maxChars - msgLength) / 2;
        if (spaces < 0) spaces = 0;
        String padding = " ".repeat(spaces);
        to.sendMessage(padding + message);
    }

    /** Helper to strip color codes when measuring length. */
    private static String stripColor(String input) {
        return input.replaceAll("(?i)§[0-9A-FK-OR]", "");
    }
}
