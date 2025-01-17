package com.notpatch.nCombat.model;

import com.notpatch.nCombat.NCombat;
import com.notpatch.nCombat.modules.Module;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Modules {

    private static HashMap<String, Boolean> modules = new HashMap<>();
    private static List<Module> moduleList = new ArrayList<>();
    private static final NCombat main = NCombat.getInstance();

    public Modules(){
        loadModules();
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

}
