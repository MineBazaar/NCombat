package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.model.Hologram;
import com.notpatch.nCombat.modules.Module;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class DamageIndicator extends Module {

    private int INDICATOR_DURATION;
    private List<String> INDICATOR_FORMAT;
    private String INDICATOR_HIT;
    private String INDICATOR_HIT_CRITICAL;


    public DamageIndicator() {
        super(ModuleManager.getModule("damage-indicator"));
        INDICATOR_DURATION = (int) ModuleManager.get("damage-indicator", "duration");
        INDICATOR_FORMAT = (List<String>) ModuleManager.get("damage-indicator", "indicator-type");
        INDICATOR_HIT = (String) ModuleManager.get("damage-indicator", "hit-format");
        INDICATOR_HIT_CRITICAL = (String) ModuleManager.get("damage-indicator", "critical-hit-format");
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)) return;

        Player attacker = (Player) e.getDamager();
        Entity victim = e.getEntity();

        sendIndicator(attacker, e.getFinalDamage(), victim);

    }

    private void sendIndicator(Player p, double damage, Entity victim) {
        List<String> displayTypes = (List<String>) ModuleManager.get("combo", "combo-display-type");
        damage = Math.round(damage * 10.0) / 10.0;
        String hitString = INDICATOR_HIT.replace("%damage%", String.valueOf(damage));
        String criticalHitString = INDICATOR_HIT_CRITICAL.replace("%damage%", String.valueOf(damage));
        hitString = ChatColor.translateAlternateColorCodes('&', hitString);
        criticalHitString = ChatColor.translateAlternateColorCodes('&', criticalHitString);
        String message = isCritical(p) ? criticalHitString : hitString;

        for (String format : INDICATOR_FORMAT) {
            switch (format.toLowerCase()) {
                case "title":
                    if(!displayTypes.contains("title")){
                        p.sendTitle(message, "", 0, INDICATOR_DURATION * 20, 0);
                    }

                case "subtitle":
                    if(!displayTypes.contains("subtitle")){
                        p.sendTitle("", message, 0, INDICATOR_DURATION * 20, 0);
                    }
                case "actionbar":
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                case "hologram":
                    Hologram hologram = new Hologram(message, victim.getWorld(),
                            victim.getLocation().getX(),
                            victim.getLocation().getY(),
                            victim.getLocation().getZ(), p.getUniqueId().toString());
                    hologram.spawn();
            }
        }
    }

    private boolean isCritical(Player damager) {
        return damager.getAttackCooldown() > 0.9F && damager.getFallDistance() > 0.0F
                && !damager.isOnGround() && !damager.isInWater() && damager.getActivePotionEffects().stream()
                .noneMatch(o -> o.getType().equals(PotionEffectType.BLINDNESS))
                && damager.getVehicle() == null && !damager.isSprinting();
    }

}
