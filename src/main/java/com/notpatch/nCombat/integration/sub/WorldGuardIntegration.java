package com.notpatch.nCombat.integration.sub;

import com.notpatch.nCombat.integration.Integration;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WorldGuardIntegration extends Integration {

    public WorldGuardIntegration() {
        super("WorldGuard", false);
    }

    @Override
    protected void setup() {


    }

    public boolean isInRegion(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));

        if (regionManager == null) return false;

        BlockVector3 playerVector = BukkitAdapter.asBlockVector(location);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(playerVector);

        for (ProtectedRegion region : regions) {
            if (region.getFlag(Flags.PVP).equals(true)){
                return true;
            }
        }
        return false;
    }

    public boolean isRegionProtected(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));

        if (regionManager == null) return false;

        BlockVector3 playerVector = BukkitAdapter.asBlockVector(location);
        ApplicableRegionSet regions = regionManager.getApplicableRegions(playerVector);

        for (ProtectedRegion region : regions) {
            if(region.getFlags().containsKey(Flags.PVP)){
                if(region.getFlag(Flags.PVP).equals(StateFlag.State.DENY))
                    return true;
            }
        }
        return false;
    }


}
