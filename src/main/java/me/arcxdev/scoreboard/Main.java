package me.arcxdev.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private ArcxScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI is not installed! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        scoreboardManager = new ArcxScoreboardManager(this);
        scoreboardManager.start();
    }

    public static Main getInstance() {
        return instance;
    }
}
