package com.notpatch.nCombat.model;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Cooldown {

    private Player p;
    private final ConcurrentHashMap<String, Integer> cooldowns;

    public Cooldown(Player p){
        this.p = p;
        cooldowns = new ConcurrentHashMap<>();
    }

    public void setCooldown(String item, int cooldown){
        cooldowns.put(item, cooldown);
    }

    public int getCooldown(String item){
        return cooldowns.getOrDefault(item, 0);
    }

    public boolean hasCooldown(String item){
        return cooldowns.containsKey(item);
    }

    public void removeCooldown(String item){
        cooldowns.remove(item);
    }

    public void reduceCooldown(String item, int amount){
        cooldowns.put(item, cooldowns.get(item) - amount);
    }
}
