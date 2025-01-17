package com.notpatch.nCombat.modules.sub;

import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.model.Toast;
import com.notpatch.nCombat.modules.Module;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class DeathMessage extends Module {

    private final String KILL_MESSAGE;
    private final List<String> KILL_MESSAGE_TYPES;

    public DeathMessage() {
        super(ModuleManager.getModule("death-message"));
        KILL_MESSAGE = (String) ModuleManager.get("death-message", "death-message");
        KILL_MESSAGE_TYPES = (List<String>) ModuleManager.get("death-message", "message-type");
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(!(e.getEntity() instanceof Player) && !(e.getEntity().getKiller() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Player killer = player.getKiller();

        for (String format : KILL_MESSAGE_TYPES) {
            switch (format.toLowerCase()) {
                case "title":
                    if(!KILL_MESSAGE_TYPES.contains("title")){
                        Bukkit.getServer().getOnlinePlayers().forEach(p -> p.sendTitle(KILL_MESSAGE.replace("%player%", player.getName()).replace("%killer%", killer.getName()), "", 0, 20, 0));
                    }

                case "subtitle":
                    if(!KILL_MESSAGE_TYPES.contains("subtitle")){
                        Bukkit.getServer().getOnlinePlayers().forEach(p -> p.sendTitle("", KILL_MESSAGE.replace("%player%", player.getName()).replace("%killer%", killer.getName()), 0, 20, 0));
                    }
                case "actionbar":
                    Bukkit.getServer().getOnlinePlayers().forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(KILL_MESSAGE.replace("%player%", player.getName()).replace("%killer%", killer.getName()))));
                case "chat":
                    Bukkit.getServer().getOnlinePlayers().forEach(p -> p.sendMessage(KILL_MESSAGE.replace("%player%", player.getName()).replace("%killer%", killer.getName())));
                case "toast":
                    Bukkit.getServer().getOnlinePlayers().forEach(p -> Toast.displayTo(p, "DIAMOND_SWORD", KILL_MESSAGE.replace("%player%", player.getName()).replace("%killer%", killer.getName()), Toast.Style.TASK));
            }
        }


    }


}
