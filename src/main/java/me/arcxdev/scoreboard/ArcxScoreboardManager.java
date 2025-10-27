package me.arcxdev.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArcxScoreboardManager {

    private final Main plugin;

    public ArcxScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboards, 0L, 40L); // every 2s
    }

    private void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateBoard(player);
        }
    }

    private void updateBoard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(color("&e&lLEGITPIXEL &7Network"));

        // ⛏️ Example variables from another class (replace with your own)
        PlayerData data = PlayerData.get(player); // Your custom player data class
        String rank = data.getRank();
        int coins = data.getCoins();
        int kills = data.getKills();
        String world = player.getWorld().getName();

        // 🟡 Hardcoded Hypixel-like scoreboard
        List<String> lines = List.of(
                "&7",
                "&fRank: " + rank,
                "&fCoins: &6" + coins,
                "&fKills: &c" + kills,
                "&fWorld: &a" + world,
                "&r",
                "&ewww.legitpixel.fun"
        );

        int score = lines.size();
        Set<String> used = new HashSet<>();
        for (String rawLine : lines) {
            String line = color(rawLine);
            while (used.contains(line)) line += ChatColor.RESET; // avoid duplicates
            used.add(line);
            objective.getScore(line).setScore(score--);
        }

        player.setScoreboard(scoreboard);
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
