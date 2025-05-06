package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.integration.Integration;
import com.notpatch.nCombat.integration.sub.GriefPreventionIntegration;
import com.notpatch.nCombat.integration.sub.WorldGuardIntegration;
import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class RegionBlocker extends Module {

    private final List<Integration> activeRegionIntegrations = new ArrayList<>();

    public RegionBlocker() {
        super(ModuleManager.getModule("region-blocker"));


    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("ncombat.bypass.region-blocker") || p.isOp()) return;

        for(Integration integration : NCombat.getInstance().getIntegrationManager().getActiveIntegrations()){
            if(!integration.isEnabled()) continue;

            if(integration instanceof WorldGuardIntegration){
                boolean protect = NCombat.getInstance().getIntegrationManager().getIntegration(WorldGuardIntegration.class).get().isRegionProtected(e.getTo());
                if(protect)
                    e.setCancelled(true);
            }

            if(integration instanceof GriefPreventionIntegration){
                boolean protect = NCombat.getInstance().getIntegrationManager().getIntegration(GriefPreventionIntegration.class).get().isInClaim(e.getTo());
                if(protect)
                    e.setCancelled(true);
            }

        }

    }



}
