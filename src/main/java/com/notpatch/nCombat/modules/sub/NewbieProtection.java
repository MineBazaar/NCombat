package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class NewbieProtection extends Module {

    public NewbieProtection(){
        super(ModuleManager.getModule("newbie-protection"));
    }

     @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(e.getPlayer().hasPlayedBefore()) return;


     }

}
