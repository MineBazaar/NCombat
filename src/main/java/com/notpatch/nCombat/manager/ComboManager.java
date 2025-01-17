package com.notpatch.nCombat.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class ComboManager {

    private static HashMap<Player, Integer> combo = new HashMap<>();
    private static HashMap<Player, Long> lastHit = new HashMap<>();

    public static int getCombo(Player player) {
        return combo.getOrDefault(player, 0);
    }

    public static void increaseCombo(Player player, long COMBO_THRESHOLD) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - getLastHit(player) > COMBO_THRESHOLD) {
            resetCombo(player);
        }

        combo.put(player, combo.getOrDefault(player, 0) + 1);
        System.out.println("Increased combo for " + player.getName() + " to " + combo.get(player));
    }

    public static void resetCombo(Player player) {
        combo.put(player, 0);
    }

    public static void setLastHit(Player player, long time) {
        lastHit.put(player, time);
    }

    public static long getLastHit(Player player) {
        return lastHit.getOrDefault(player, 0L);
    }
}
