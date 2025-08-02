package net.metroCore.Core.database;

import net.metroCore.Core.logging.CoreLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final JavaPlugin plugin;
    private Connection connection;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration config = plugin.getConfig();
        String host = config.getString("database.host");
        String database = config.getString("database.name");
        String username = config.getString("database.user");
        String password = config.getString("database.password");
        int port = config.getInt("database.port");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";

        try {
            connection = DriverManager.getConnection(url, username, password);
            CoreLogger.info("Connected to MySQL database.");
        } catch (SQLException e) {
            CoreLogger.error("Could not connect to the database: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                CoreLogger.info("Disconnected from database.");
            }
        } catch (SQLException e) {
            CoreLogger.error("Error disconnecting from database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
