package com.notpatch.nCombat.integration.sub;

import com.notpatch.nCombat.integration.Integration;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;

public class GriefPreventionIntegration extends Integration {

    public GriefPreventionIntegration() {
        super("GriefPrevention", false);
    }

    @Override
    protected void setup() {

    }

    public boolean isInClaim(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if(claim != null){
            return true;
        }
        return false;
    }


}
