package me.arcxdev.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public class ArcxScoreboardManager {

    private final Main plugin;
    private final Map<Player, Scoreboard> playerBoards = new HashMap<>();

    public ArcxScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboards, 0L, 40L); // 2 seconds
    }

    private void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateBoard(player);
        }
    }

    private void updateBoard(Player player) {
        String worldName = player.getWorld().getName();
        ConfigurationSection worldConfig = plugin.getConfig().getConfigurationSection("worlds." + worldName);

        if (worldConfig == null || !worldConfig.getBoolean("enabled")) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            return;
        }

        List<String> lines = worldConfig.getStringList("lines");
        String title = PlaceholderAPI.setPlaceholders(player, worldConfig.getString("title", ""));

        ScoreboardManager bukkitManager = Bukkit.getScoreboardManager();
        Scoreboard board = bukkitManager.getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(title);

        int score = lines.size();
        Set<String> used = new HashSet<>();
        for (String rawLine : lines) {
            String line = PlaceholderAPI.setPlaceholders(player, rawLine);
            while (used.contains(line)) line += "§r"; // Prevent duplicate lines
            used.add(line);
            obj.getScore(line).setScore(score--);
        }

        player.setScoreboard(board);
    }
}
