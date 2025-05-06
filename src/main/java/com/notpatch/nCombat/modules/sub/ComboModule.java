package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.manager.ComboManager;
import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.modules.Module;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class ComboModule extends Module {

    private int COMBO_THRESHOLD;
    private List<String> COMBO_TYPES;
    private String COMBO_FORMAT_DEFAULT;
    private HashMap<Integer, String> COMBO_FORMAT = new HashMap<>();

    public ComboModule() {
        super(ModuleManager.getModule("combo"));
        COMBO_THRESHOLD = (int) ModuleManager.get("combo", "threshold") * 1000;
        COMBO_FORMAT_DEFAULT = (String) ModuleManager.get("combo", "combo-format-default");
        COMBO_TYPES = (List<String>) ModuleManager.get("combo", "combo-display-type");
        ConfigurationSection section = ModuleManager.getSection("combo", "combo-formats");
        for(String key : section.getKeys(false)){
            COMBO_FORMAT.put(Integer.parseInt(key), section.getString(key));
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player player && !(e.getEntity() instanceof Player))) return;
        Player victim = (Player) e.getEntity();

        if(player == victim) return;

        long currentTimeMillis = System.currentTimeMillis();

        int combo = ComboManager.getCombo(player);
        ComboManager.increaseCombo(player, COMBO_THRESHOLD);
        ComboManager.setLastHit(player, currentTimeMillis);

        sendComboMessage(player, combo);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();
        if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
            if(ComboManager.getCombo(player) > 0 && getEntityInLineOfSight(player, 4) == null){
                ComboManager.resetCombo(player);
            }
        }
    }

    private String getComboMessage(int combo){
        if(COMBO_FORMAT.containsKey(combo)){
            return ChatColor.translateAlternateColorCodes('&', COMBO_FORMAT.get(combo));
        }
        return ChatColor.translateAlternateColorCodes('&', COMBO_FORMAT_DEFAULT.replace("%combo%", String.valueOf(combo)));
    }

    private void sendComboMessage(Player player, int combo){
        for(String type : COMBO_TYPES){
            switch (type.toLowerCase()){
                case "actionbar":
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getComboMessage(combo)));
                    break;
                case "title":
                    player.sendTitle(getComboMessage(combo), "", 0, 20, 0);
                    break;
                case "subtitle":
                    player.sendTitle("", getComboMessage(combo), 0, 20, 0);
                    break;
            }
        }
    }

    public Entity getEntityInLineOfSight(Player player, double range) {
        Vector direction = player.getLocation().getDirection();

        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getLocation(), direction, range);

        if (result != null && result.getHitEntity() != null) {
            return result.getHitEntity();
        }

        return null;
    }

}
