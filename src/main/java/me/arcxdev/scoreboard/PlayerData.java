package me.arcxdev.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    private static final Map<Player, PlayerData> playerDataMap = new HashMap<>();

    private final Player player;
    private String rank;
    private int coins;
    private int kills;

    public PlayerData(Player player) {
        this.player = player;
        this.rank = "Default";
        this.coins = 0;
        this.kills = 0;
        playerDataMap.put(player, this);
    }

    public static PlayerData get(Player player) {
        return playerDataMap.computeIfAbsent(player, PlayerData::new);
    }

    public Player getPlayer() {
        return player;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
