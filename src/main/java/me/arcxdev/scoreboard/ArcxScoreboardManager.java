package me.arcxdev.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ArcxScoreboardManager {

    private final Main plugin;

    public ArcxScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboards, 0L, 40L); // every 2 seconds
    }

    private void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateBoard(player);
        }
    }

    private void updateBoard(Player player) {
        String world = player.getWorld().getName();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds." + world);

        if (section == null || !section.getBoolean("enabled")) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            return;
        }

        List<String> lines = section.getStringList("lines");
        String title = PlaceholderAPI.setPlaceholders(player, section.getString("title"));

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("dummy", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(title);

        int score = lines.size();
        Set<String> used = new HashSet<>();
        for (String rawLine : lines) {
            String line = PlaceholderAPI.setPlaceholders(player, rawLine);
            while (used.contains(line)) line += "§r";
            used.add(line);
            obj.getScore(line).setScore(score--);
        }

        player.setScoreboard(scoreboard);
    }
}