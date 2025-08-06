// src/main/java/net/metroCore/Modules/metroedit/util/IOUtil.java
package net.metroCore.Modules.metroedit.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple file operations used across MetroEdit.
 */
public final class IOUtil {
    private IOUtil() {}

    /** Ensure that a folder exists on disk. */
    public static void ensureDirectory(File dir) throws IOException {
        Path p = dir.toPath();
        if (!Files.exists(p)) {
            Files.createDirectories(p);
        }
    }

    /** Read entire text file into a single String. */
    public static String readAllText(File file) throws IOException {
        return Files.readString(file.toPath());
    }

    /** Write entire text content to a file. */
    public static void writeAllText(File file, String content) throws IOException {
        ensureDirectory(file.getParentFile());
        Files.writeString(file.toPath(), content);
    }
}
