package com.notpatch.nCombat.model;

import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.manager.HologramManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class Hologram {

    private String text;
    private double x;
    private double y;
    private double z;
    private String id;
    private World world;

    public Hologram(String text, World world, double x, double y, double z, String id) {
        this.text = text;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getId() {
        return id;
    }

    public World getWorld() {
        return world;
    }


    public void spawn() {
        ArmorStand existingArmorStand = HologramManager.getActiveHolograms().get(getId());
        if (existingArmorStand != null && !existingArmorStand.isDead()) {
            existingArmorStand.remove();
            HologramManager.getActiveHolograms().remove(getId());
        }

        ArmorStand armorStand = getWorld().spawn(new Location(getWorld(), getX(), getY(), getZ()), ArmorStand.class);
        armorStand.setCustomName(getText());
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setBasePlate(false);
        armorStand.setSmall(true);

        HologramManager.getActiveHolograms().put(getId(), armorStand);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!armorStand.isDead()) {
                    armorStand.remove();
                    HologramManager.getActiveHolograms().remove(getId());
                }

            }
        }.runTaskLater(NCombat.getInstance(), 20L * 2); // 2 saniye sonra kaldır
    }

}
