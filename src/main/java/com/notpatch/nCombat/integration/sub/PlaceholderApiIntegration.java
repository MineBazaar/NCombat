package com.notpatch.nCombat.integration.sub;

import com.notpatch.nCombat.hook.PlaceholderAPIHook;
import com.notpatch.nCombat.integration.Integration;

public class PlaceholderApiIntegration extends Integration {

    public PlaceholderApiIntegration() {
        super("PlaceholderAPI", false);
    }

    @Override
    protected void setup() {
        new PlaceholderAPIHook().register();
    }

}
