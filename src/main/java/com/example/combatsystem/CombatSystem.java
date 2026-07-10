package com.example.combatsystem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "combatsystem", name = "Combat System", version = "1.0")
public class CombatSystem {
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CombatConfig.loadConfig(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Серверна сторона
        MinecraftForge.EVENT_BUS.register(new CombatEventHandler());
        
        // Клієнтська сторона
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new CooldownIndicatorRenderer());
        }
    }
}
