package com.example.combatsystem;

import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CombatConfig {
    
    public static Configuration config;
    
    public static boolean COMBAT_SYSTEM_ENABLED = true;
    public static boolean ITEM_ANIMATION_ENABLED = true;
    public static int GLOBAL_COOLDOWN = 10;
    
    public static Map<String, Integer> TOOL_COOLDOWNS = new HashMap<>();
    
    public static void loadConfig(File file) {
        config = new Configuration(file);
        
        try {
            config.load();
            
            COMBAT_SYSTEM_ENABLED = config.getBoolean(
                "enabled",
                "general",
                true,
                "Вмикнути/вимкнути новую боєву систему");
            
            ITEM_ANIMATION_ENABLED = config.getBoolean(
                "itemAnimation",
                "general",
                true,
                "Вмикнути/вимкнути анімацію предмета при ударі");
            
            GLOBAL_COOLDOWN = config.getInt(
                "globalCooldown",
                "general",
                10,
                5,
                40,
                "Глобальний cooldown для всіх інструментів (в тіках)");
            
            loadToolCooldown("wooden_sword", 10);
            loadToolCooldown("stone_sword", 9);
            loadToolCooldown("iron_sword", 8);
            loadToolCooldown("diamond_sword", 7);
            loadToolCooldown("gold_sword", 12);
            
            loadToolCooldown("wooden_pickaxe", 12);
            loadToolCooldown("stone_pickaxe", 11);
            loadToolCooldown("iron_pickaxe", 10);
            loadToolCooldown("diamond_pickaxe", 9);
            loadToolCooldown("gold_pickaxe", 14);
            
            loadToolCooldown("wooden_axe", 11);
            loadToolCooldown("stone_axe", 10);
            loadToolCooldown("iron_axe", 9);
            loadToolCooldown("diamond_axe", 8);
            loadToolCooldown("gold_axe", 13);
            
            loadToolCooldown("wooden_shovel", 13);
            loadToolCooldown("stone_shovel", 12);
            loadToolCooldown("iron_shovel", 11);
            loadToolCooldown("diamond_shovel", 10);
            loadToolCooldown("gold_shovel", 15);
            
            loadToolCooldown("wooden_hoe", 15);
            loadToolCooldown("stone_hoe", 14);
            loadToolCooldown("iron_hoe", 13);
            loadToolCooldown("diamond_hoe", 12);
            loadToolCooldown("gold_hoe", 17);
            
            loadToolCooldown("bare_hand", 12);
            
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
    
    private static void loadToolCooldown(String toolName, int defaultValue) {
        int cooldown = config.getInt(
            toolName,
            "toolCooldowns",
            defaultValue,
            5,
            100,
            "Cooldown для " + toolName + " (в тіках)");
        
        TOOL_COOLDOWNS.put(toolName, cooldown);
    }
    
    public static int getCooldownForTool(String toolName) {
        if (TOOL_COOLDOWNS.containsKey(toolName)) {
            return TOOL_COOLDOWNS.get(toolName);
        }
        return GLOBAL_COOLDOWN;
    }
}
