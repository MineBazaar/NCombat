package com.notpatch.nCombat;

import com.notpatch.nCombat.manager.IntegrationManager;
import com.notpatch.nCombat.manager.ModuleManager;
import com.notpatch.nCombat.task.TagRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public final class NCombat extends JavaPlugin {

    private static NCombat instance;

    private LanguageLoader languageLoader;
    private ModuleManager moduleManager;
    private IntegrationManager integrationManager;

    @Override
    public void onEnable() {
        instance = this;

        // Config section
        saveDefaultConfig();
        saveConfig();

        // Integration section
        integrationManager = new IntegrationManager();
        integrationManager.initializeIntegrations();

        // Language section
        languageLoader = new LanguageLoader(this);
        languageLoader.loadLangs();

        // Module section
        moduleManager = new ModuleManager();

        // Tasks

        TagRunnable tagRunnable = new TagRunnable();
        tagRunnable.runTaskTimer(this, 20, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NCombat getInstance() {
        return instance;
    }

    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }
}
