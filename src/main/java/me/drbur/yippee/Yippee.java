package me.drbur.yippee;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

public class Yippee extends JavaPlugin {
    private MyServer myServer;

    @Override
    public void onEnable() {
        getLogger().info("Yippee plugin has been enabled!");

        // Create the server on a separate thread
        myServer = new MyServer(8080);
        Thread serverThread = new Thread(() -> {
            try {
                myServer.start();
            } catch (Exception e) {
                getLogger().severe("Failed to start Yippee server: " + e.getMessage());
            }
        });
        serverThread.start();
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
}