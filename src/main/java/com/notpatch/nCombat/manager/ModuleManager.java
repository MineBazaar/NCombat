package com.notpatch.nCombat.manager;

import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.modules.Module;
import com.notpatch.nCombat.modules.sub.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class ModuleManager {

    private static final HashMap<String, Boolean> modules = new HashMap<>();
    private static final NCombat main = NCombat.getInstance();

    public ModuleManager(){
        loadModules();
        enableModules();
    }

    public void loadModules(){
        Configuration configuration = main.getConfig();
        for(String line : configuration.getConfigurationSection("modules").getKeys(false)){
            modules.put(line, configuration.getBoolean("modules." + line + ".enabled"));
        }
    }

    public static boolean getModule(String module){
        return modules.get(module);
    }

    public static Object get(String module, String setting){
        return main.getConfig().get("modules." + module + "." + setting);
    }

    public static ConfigurationSection getSection(String module, String subSection){
        return main.getConfig().getConfigurationSection("modules." + module + "." + subSection);
    }

    public void enableModules(){
        List<Module> modules = List.of(
                new ItemCooldown(),
                new CombatTag(),
                new NewbieProtection(),
                new DamageIndicator(),
                new ComboModule()

        );
        modules.forEach(Module::enable);
    }

    public void disableModules(){
        List<Module> modules = List.of(
                new ItemCooldown(),
                new CombatTag(),
                new NewbieProtection(),
                new DamageIndicator(),
                new ComboModule()
        );
        modules.forEach(Module::disable);
    }

}
