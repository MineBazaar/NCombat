package com.notpatch.nCombat.task;

import com.notpatch.nCombat.modules.sub.CombatTag;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TagRunnable extends BukkitRunnable {

    @Override
    public void run() {

        for(Player player : CombatTag.getCombatTag().keySet()){
            int time = CombatTag.getCombatTag().get(player);
            if(time <= 0){
                CombatTag.getCombatTag().remove(player);
                continue;
            }
            CombatTag.getCombatTag().put(player, time - 1);
            player.sendMessage("You have " + time + " seconds left in combat.");
        }

    }
}
