package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import java.util.Map;

public class CombatEventHandler {
    
    private static Map<String, Long> lastAttack = new HashMap<>();
    
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.entityPlayer;
        String name = player.getCommandSenderName();
        long now = System.currentTimeMillis();
        
        // Перевіримо чи можна атакувати
        if (lastAttack.containsKey(name)) {
            long lastTime = lastAttack.get(name);
            long diff = now - lastTime;
            
            // 500ms = 0.5 секунди
            if (diff < 500) {
                event.setCanceled(true);
                return;
            }
        }
        
        lastAttack.put(name, now);
    }
    
    public static boolean canAttack(EntityPlayer player) {
        String name = player.getCommandSenderName();
        long now = System.currentTimeMillis();
        
        if (!lastAttack.containsKey(name)) {
            return true;
        }
        
        long lastTime = lastAttack.get(name);
        return (now - lastTime) >= 500;
    }
}
