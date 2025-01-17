package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.LanguageLoader;
import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.manager.CooldownManager;
import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.model.Cooldown;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemCooldown extends Module {

    private LanguageLoader languageLoader;


    public ItemCooldown() {
        super(ModuleManager.getModule("item-cooldown"));
        languageLoader = NCombat.getInstance().getLanguageLoader();
    }

    @EventHandler
    public void onEnderPearlCooldown(ProjectileLaunchEvent e) {
        if(!(e.getEntity() instanceof EnderPearl)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        Player p = (Player) e.getEntity().getShooter();

        String permission = "ncombat.bypass.cooldown-enderpearl";
        int cooldown = (int) ModuleManager.get("item-cooldown", "cooldowns.ENDER_PEARL") * 20;

        if(p.isOp() || p.hasPermission(permission)){
            return;
        }

        Bukkit.getScheduler().runTaskLater(NCombat.getInstance(), () -> {
            p.setCooldown(Material.ENDER_PEARL, cooldown);
        }, 10L);

    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if(item == null) return;
        String itemName = item.getType().name().toUpperCase();

        if(ModuleManager.get("item-cooldown", "cooldowns." + itemName) == null) return;
        String permission = "ncombat.bypass.cooldown-" + itemName;

        Cooldown cooldown = CooldownManager.getCooldown(p);
        if(cooldown.hasCooldown(itemName)){
            p.sendMessage(languageLoader.get("cooldown-message").replace("%cooldown%", String.valueOf(cooldown.getCooldown(itemName))).replace("%item%", itemName));
            e.setCancelled(true);
            return;
        }

        if(p.isOp() || p.hasPermission(permission)){
            return;
        }

        int cooldownInt = (int) ModuleManager.get("item-cooldown", "cooldowns." + itemName);
        cooldown.setCooldown(itemName, cooldownInt);
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown.reduceCooldown(itemName, 1);
                if(cooldown.getCooldown(itemName) == 0){
                    cooldown.removeCooldown(itemName);
                    p.sendMessage(languageLoader.get("cooldown-use").replace("%item%", itemName));
                    cancel();
                }
            }
        }.runTaskTimer(NCombat.getInstance(), 0L, 20L);

    }

}
