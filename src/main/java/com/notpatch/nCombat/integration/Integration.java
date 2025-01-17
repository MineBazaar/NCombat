package com.notpatch.nCombat.integration;

import com.notpatch.nCombat.NCombat;
import org.bukkit.Bukkit;

public abstract class Integration {

    private final String pluginName;
    private final boolean hardDependency;

    public Integration(String pluginName, boolean hardDependency) {
        this.pluginName = pluginName;
        this.hardDependency = hardDependency;
    }

    public boolean isHardDependency() {
        return hardDependency;
    }

    public boolean isEnabled(){
        return NCombat.getInstance().getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void initialize() {
        if (isEnabled()) {
            setup();
            NCombat.getInstance().getLogger().info("Successfully hooked into " + pluginName + "!");
        } else if (hardDependency) {
            Bukkit.getPluginManager().disablePlugin(NCombat.getInstance());
            throw new IllegalStateException("Required plugin '" + pluginName + "' is not installed!");
        }
    }

    protected abstract void setup();
}