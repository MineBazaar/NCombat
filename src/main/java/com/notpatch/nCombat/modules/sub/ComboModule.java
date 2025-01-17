package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.List;

public class Combo extends Module {

    private int COMBO_THRESHOLD;
    private List<String> COMBO_TYPES;
    private String COMBO_FORMAT_DEFAULT;
    private HashMap<Integer, String> COMBO_FORMAT = new HashMap<>();

    public Combo() {
        super(ModuleManager.getModule("combo"));
        COMBO_THRESHOLD = (int) ModuleManager.get("combo", "threshold");
        COMBO_FORMAT_DEFAULT = (String) ModuleManager.get("combo", "combo-format-default");
        COMBO_TYPES = (List<String>) ModuleManager.get("combo", "combo-display-type");
        ConfigurationSection section = ModuleManager.getSection("combo", "combo-formats");
        for(String key : section.getKeys(false)){
            COMBO_FORMAT.put(Integer.parseInt(key), section.getString(key));
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player || !(e.getEntity() instanceof Player))) return;

    }

}
