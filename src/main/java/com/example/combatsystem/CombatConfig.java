package com.example.combatsystem;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class CombatConfig {
    
    public static boolean ENABLED = true;
    
    public static void loadConfig(File file) {
        Configuration cfg = new Configuration(file);
        cfg.load();
        ENABLED = cfg.getBoolean("enabled", "general", true, "Enable combat system");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }
}
