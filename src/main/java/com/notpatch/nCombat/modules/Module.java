package com.notpatch.nCombat.modules;

import com.notpatch.nCombat.NCombat;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Module implements Listener {

    private final boolean enabled;

    public Module(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable(){
        if(enabled){
            Bukkit.getPluginManager().registerEvents(this, NCombat.getInstance());
        }
    }

    public void disable(){
        if(enabled){
            HandlerList.unregisterAll(this);
        }
    }

}
