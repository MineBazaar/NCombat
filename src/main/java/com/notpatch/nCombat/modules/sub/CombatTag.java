package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.LanguageLoader;
import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombatTag extends Module {

    public int COMBAT_TAG_TIME;
    public static Map<Player, Integer> combatTag = new ConcurrentHashMap<>();

    private final LanguageLoader languageLoader;

    private final List<String> DISABLED_ACTIONS_BLOCK_PLACE;
    private final List<String> DISABLED_ACTIONS_BLOCK_BREAK;
    private final List<String> DISABLED_ACTIONS_GENERAL;
    private final List<String> DISABLED_ACTIONS_ITEM_CONSUME;
    private final List<String> DISABLE_ITEM_DROP;
    private final List<String> DISABLE_ITEM_PICKUP;
    private final List<String> DISABLE_COMMAND;

    private static boolean DISABLE_FLIGHT;
    private static boolean SHOULD_DIE_ON_LEAVE;


    private final Set<Player> riptidePlayers = ConcurrentHashMap.newKeySet();

    public CombatTag() {
        super(ModuleManager.getModule("combat-tag"));
        languageLoader = NCombat.getInstance().getLanguageLoader();
        COMBAT_TAG_TIME = Objects.requireNonNullElse((int) ModuleManager.get("combat-tag", "combat-tag-time"), 15);
        DISABLED_ACTIONS_BLOCK_PLACE = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.BLOCK_PLACE");
        DISABLED_ACTIONS_BLOCK_BREAK = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.BLOCK_BREAK");
        DISABLED_ACTIONS_GENERAL = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.GENERAL");
        DISABLED_ACTIONS_ITEM_CONSUME = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.ITEM_CONSUME");
        DISABLE_ITEM_DROP = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.ITEM_DROP");
        DISABLE_ITEM_PICKUP = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.ITEM_PICKUP");
        DISABLE_COMMAND = (List<String>) ModuleManager.get("combat-tag", "disabled-actions.DISABLE_COMMAND");

        DISABLE_FLIGHT = (boolean) ModuleManager.get("combat-tag", "disable-flight");
        SHOULD_DIE_ON_LEAVE = (boolean) ModuleManager.get("combat-tag", "death-on-leave");

    }

    /*
    * Combat Tag
    * */

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        boolean disableFlight = DISABLE_FLIGHT;

        if(!hasCombatTag(attacker)){
            if(attacker.hasPermission("ncombat.bypass.tag")) return;
            addCombatTag(attacker);
            if(disableFlight){
                attacker.setAllowFlight(false);
            }
        }

        if(!hasCombatTag(victim)){
            if(victim.hasPermission("ncombat.bypass.tag")) return;
            addCombatTag(victim);
            if(disableFlight){
                victim.setAllowFlight(false);
            }
        }
    }

    /*
    * Disable Ender Pearl
    * */

    @EventHandler(priority = EventPriority.HIGH)
    public void onEnderPearlCooldown(ProjectileLaunchEvent e) {
        if(!(e.getEntity() instanceof EnderPearl)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        Player p = (Player) e.getEntity().getShooter();

        if(p.hasPermission("ncombat.bypass.enderpearl")) return;
        if(!hasCombatTag(p)) return;

        List<String> actions = DISABLED_ACTIONS_GENERAL;

        if(actions.contains("ENDER_PEARL")){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-enderpearl"));
        }
    }

    /*
    * Disable Riptide
    * */

    @EventHandler
    public void onRiptide(PlayerRiptideEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("ncombat.bypass.trident")) return;
        if(!hasCombatTag(p)) return;

        List<String> actions = DISABLED_ACTIONS_GENERAL;

        if(actions.contains("TRIDENT")){
            riptidePlayers.add(p);
        }
    }

    /*
    * Disable block place
    * */

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("ncombat.bypass.block-place")) return;
        if(!hasCombatTag(p)) return;

        String block = e.getBlock().getType().name().toUpperCase();
        List<String> blocks = DISABLED_ACTIONS_BLOCK_PLACE;

        if (blocks.isEmpty()) return;

        if(blocks.contains("*") || blocks.contains("all") || blocks.contains(block)){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-block-place").replace("%block%", block));
        }
    }

    /*
    * Disable block break
    * */

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();

        if(p.hasPermission("ncombat.bypass.block-break")) return;

        if(!hasCombatTag(p)) return;

        String block = e.getBlock().getType().name().toUpperCase();
        List<String> blocks = DISABLED_ACTIONS_BLOCK_BREAK;

        if (blocks.isEmpty()) return;

        if(blocks.contains("*") || blocks.contains("all") || blocks.contains(block)){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-block-break").replace("%block%", block));
        }
    }

    /*
     * Item drop
     * */

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();

        if(p.hasPermission("ncombat.bypass.item-drop")) return;
        if(!hasCombatTag(p)) return;

        String item = e.getItemDrop().getItemStack().getType().name();
        List<String> items = DISABLE_ITEM_DROP;

        if(items.isEmpty()) return;

        if(items.contains("*") || items.contains("all") || items.contains(item)){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-item-drop").replace("%item%", item));
        }
    }

    /*
     * Item pickup
     * */

    @EventHandler
    public void onDrop(PlayerPickupItemEvent e){
        Player p = e.getPlayer();

        if(p.hasPermission("ncombat.bypass.item-pickup")) return;
        if(!hasCombatTag(p)) return;

        String item = e.getItem().getItemStack().getType().name();
        List<String> items = DISABLE_ITEM_PICKUP;

        if(items.isEmpty()) return;

        if(items.contains("*") || items.contains("all") || items.contains(item)){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-item-pickup").replace("%item%", item));
        }
    }

    /*
    * Command use
    * */

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        //if(p.hasPermission("combat-tag.bypass")) return;

        if(p.hasPermission("ncombat.bypass.command")) return;
        if(!hasCombatTag(p)) return;

        String command = e.getMessage().split(" ")[0].replace("/", "");
        List<String> commands = DISABLE_COMMAND;

        if(commands.isEmpty()) return;

        if(commands.contains("*") || commands.contains("all") || commands.contains(command)){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-use-command"));
        }
    }

    /*
     * Elytra
     * */

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        //if(p.hasPermission("combat-tag.bypass")) return;

        if(p.hasPermission("ncombat.bypass.glide")) return;
        if(!hasCombatTag(p)) return;

        List<String> disableGlide = DISABLED_ACTIONS_GENERAL;

        if(disableGlide.isEmpty()) return;

        if(disableGlide.contains("ELYTRA")){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-glide"));
        }
    }

    /*
    * Elytra, Trident
    * */

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        //if(p.hasPermission("combat-tag.bypass")) return;

        if(p.hasPermission("ncombat.bypass.glide")) return;
        if(p.hasPermission("ncombat.bypass.trident")) return;
        if(!hasCombatTag(p)) return;

        List<String> disableGlide = DISABLED_ACTIONS_GENERAL;

        if(disableGlide.isEmpty()) return;

        if(disableGlide.contains("ELYTRA")){
            if(p.isGliding()){
                p.setGliding(false);
                p.sendMessage(languageLoader.get("disable-glide"));
            }
        }

        if(riptidePlayers.contains(p)){
            Vector reduce = new Vector(p.getVelocity().getX() * -0.5, -1, p.getVelocity().getZ() * -0.5);
            p.setVelocity(reduce);
            if(p.isOnGround() || p.isSwimming()){
                riptidePlayers.remove(p);
            }
        }

    }

    /*
    * Item Consume
    * */

    @EventHandler(priority = EventPriority.HIGH)
    public void onConsume(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        //if(p.hasPermission("combat-tag.bypass")) return;

        if(p.hasPermission("ncombat.bypass.consume")) return;
        if(!hasCombatTag(p)) return;

        Material item = e.getItem().getType();

        List<String> items = DISABLED_ACTIONS_ITEM_CONSUME;

        if(items.isEmpty()) return;

        if(items.contains("*") || items.contains("all") || items.contains(item.name())){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-consume").replace("%item%", item.name()));
        }
    }

    /*
    * Death on leave
    * */

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(hasCombatTag(player)){
            boolean shouldDie = SHOULD_DIE_ON_LEAVE;
            if(shouldDie){
                player.setHealth(0);
            }
            combatTag.remove(player);
        }
    }

    /*
    * Disable totem of undying
    * */

    @EventHandler
    public void onTotem(EntityResurrectEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();

        if(p.hasPermission("ncombat.bypass.totem")) return;
        if(!hasCombatTag(p)) return;

        List<String> actions = DISABLED_ACTIONS_GENERAL;

        if(actions.isEmpty()) return;

        if(actions.contains("TOTEM_OF_UNDYING")){
            e.setCancelled(true);
            p.sendMessage(languageLoader.get("disable-totem"));
        }
    }

    public void addCombatTag(Player player){
        combatTag.put(player, COMBAT_TAG_TIME);
    }

    public boolean hasCombatTag(Entity player){
        return combatTag.containsKey(player);
    }

    public static Map<Player, Integer> getCombatTag() {
        return combatTag;
    }
}
