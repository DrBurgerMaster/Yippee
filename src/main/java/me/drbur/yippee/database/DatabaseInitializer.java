package me.drbur.yippee.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initTables(Connection conn) throws SQLException {

        // Create the teams table
        String createTeamsTableSQL = "CREATE TABLE IF NOT EXISTS teams (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "war_currency INT DEFAULT 0" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTeamsTableSQL);
            System.out.println("Teams table created.");
        }

        // Create the users table
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "login_username VARCHAR(255) DEFAULT NULL," +
                "login_password VARCHAR(255) DEFAULT NULL," +
                "minecraft_uuid VARCHAR(36)," +
                "team_id VARCHAR(36) DEFAULT NULL," +
                "token VARCHAR(255) DEFAULT NULL," +
                "FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTableSQL);
            System.out.println("Users table created.");
        }

        // Create the cells table
        String createCellsTableSQL = "CREATE TABLE IF NOT EXISTS cells (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "team_id VARCHAR(36)," +
                "x_coordinate INT," +
                "y_coordinate INT," +
                "devastation INT DEFAULT 0," +
                "FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createCellsTableSQL);
            System.out.println("Cells table created.");
        }

        // Create the units table
        String createUnitsTableSQL = "CREATE TABLE IF NOT EXISTS units (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "type VARCHAR(255)," +
                "health INT DEFAULT 0," +
                "skill_level INT DEFAULT 0," +
                "team_id VARCHAR(36)," +
                "deployed BOOLEAN DEFAULT false," +
                "cell_id VARCHAR(36)," +
                "FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL," +
                "FOREIGN KEY (cell_id) REFERENCES cells(id) ON DELETE SET NULL" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUnitsTableSQL);
            System.out.println("Units table created.");
        }
    }
}
