package me.drbur.yippee;

import me.drbur.yippee.database.DatabaseInitializer;
import me.drbur.yippee.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Yippee extends JavaPlugin {
    private MyServer myServer;

    @Override
    public void onEnable() {
        getLogger().info("Yippee plugin has been enabled!");

        // Create the plugin directory
        createPluginDirectory();
        getLogger().info("Yippee plugin directory has been created!");

        // Load the default config if it doesn't exist
        saveDefaultConfig();
        reloadConfig();

        // Initialize the database tables
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseInitializer.initTables(conn);
            getLogger().info("Yippee database tables have been initialized!");
        } catch (SQLException e) {
            getLogger().severe("Failed to initialize database tables: " + e.getMessage());
            e.printStackTrace();
        }

        // Create the server on a separate thread
        myServer = new MyServer(8080);
        Thread serverThread = new Thread(() -> {
            try {
                myServer.start();
                getLogger().info("Yippee server has been enabled! http://localhost:8080/app");
            } catch (Exception e) {
                getLogger().severe("Failed to start Yippee server: " + e.getMessage());
            }
        });
        serverThread.start();

        // Test the database by adding a user
        testDatabase();

        // Test the database by getting user information
        printTestUser();
    }

    @Override
    public void onDisable() {
        getLogger().info("Yippee plugin has been disabled!");
        // Stop the server
        try {
            myServer.stop();
        } catch (Exception e) {
            getLogger().severe("Failed to stop Yippee server: " + e.getMessage());
        }
    }

    public void createPluginDirectory() {
        File pluginDir = new File(getDataFolder().getParentFile(), getName());
        if (!pluginDir.exists()) {
            if (!pluginDir.mkdirs()) {
                getLogger().warning("Could not create plugin directory!");
            }
        }

        File configFile = new File(pluginDir, "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }


    public static Yippee getInstance() {
        return JavaPlugin.getPlugin(Yippee.class);
    }

    private void testDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "INSERT INTO users (id, login_username, login_password, minecraft_uuid) " +
                        "VALUES ('1', 'johndoe', 'password123', '123')";
                stmt.executeUpdate(sql);
                getLogger().info("Added test user to database.");
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to add test user to database: " + e.getMessage());
        }
    }

    private void printTestUser() {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "SELECT id, login_username, login_password FROM users WHERE id = '1'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String id = rs.getString("id");
                    String username = rs.getString("login_username");
                    String password = rs.getString("login_password");
                    getLogger().info("Test user information:");
                    getLogger().info("ID: " + id);
                    getLogger().info("Username: " + username);
                    getLogger().info("Password: " + password);
                }
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to retrieve test user information: " + e.getMessage());
        }
    }

}