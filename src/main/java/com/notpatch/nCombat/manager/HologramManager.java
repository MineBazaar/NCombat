package com.notpatch.nCombat.manager;

import org.bukkit.entity.ArmorStand;

import java.util.HashMap;

public class HologramManager {

    private static HashMap<String, ArmorStand> activeHolograms = new HashMap<>();

    public static HashMap<String, ArmorStand> getActiveHolograms() {
        return activeHolograms;
    }
}
