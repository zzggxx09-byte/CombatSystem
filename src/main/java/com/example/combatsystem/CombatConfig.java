package com.example.combatsystem;

import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CombatConfig {
    
    public static Configuration config;
    
    public static boolean COMBAT_SYSTEM_ENABLED = true;
    public static int GLOBAL_COOLDOWN = 10; // тіків (10 = 0.5 секунди)
    
    public static Map<String, Integer> TOOL_COOLDOWNS = new HashMap<>();
    
    public static void loadConfig(File file) {
        config = new Configuration(file);
        
        try {
            config.load();
            
            COMBAT_SYSTEM_ENABLED = config.getBoolean(
                "enabled",
                "general",
                true,
                "Вмикнути/вимкнути боєву систему");
            
            GLOBAL_COOLDOWN = config.getInt(
                "globalCooldown",
                "general",
                10,
                5,
                40,
                "Глобальний cooldown (в тіках: 20 = 1 секунда)");
            
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
