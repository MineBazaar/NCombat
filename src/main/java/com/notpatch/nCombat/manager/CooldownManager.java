package com.notpatch.nCombat.manager;

import com.notpatch.nCombat.model.Cooldown;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private static final Map<Player, Cooldown> playerCooldowns = new HashMap<>();

    public static Cooldown getCooldown(Player player) {
        return playerCooldowns.computeIfAbsent(player, Cooldown::new);
    }

    public static void removeCooldown(Player player) {
        playerCooldowns.remove(player);
    }

}
