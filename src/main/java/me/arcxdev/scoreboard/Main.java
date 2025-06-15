package me.arcxdev.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Main extends JavaPlugin {

    private static Main instance;
    private ArcxScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        String licenseKey = getConfig().getString("license");
        if (!checkLicense(licenseKey)) {
            getLogger().severe("Invalid license key! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI is not installed! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        scoreboardManager = new ArcxScoreboardManager(this);
        scoreboardManager.start();
    }

    private boolean checkLicense(String key) {
        try {
            URL url = new URL("https://legitpixel.infinityfreeapp.com/licence.php?key=" + key);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = in.readLine().trim();
            in.close();
            return response.equalsIgnoreCase("VALID");
        } catch (Exception e) {
            getLogger().severe("Could not contact license server: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("arcxreload")) {
            if (player.hasPermission("arcxscoreboard.reload")) {
                reloadConfig();
                scoreboardManager.start();
                player.sendMessage("§aArcxScoreboard reloaded.");
            } else {
                player.sendMessage("§cYou don't have permission.");
            }
            return true;
        }

        return false;
    }

    public static Main getInstance() {
        return instance;
    }
}
