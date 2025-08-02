package net.metroCore.Core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseTable {

    protected final Connection connection;

    public DatabaseTable(Connection connection) {
        this.connection = connection;
    }

    public abstract void createTable();

    protected void executeUpdate(String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
