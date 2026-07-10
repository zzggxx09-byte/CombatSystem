package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import java.util.Map;

public class CombatEventHandler {
    
    private static Map<String, Long> playerCooldownTime = new HashMap<>();
    
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (!CombatConfig.COMBAT_SYSTEM_ENABLED) {
            return;
        }
        
        EntityPlayer player = event.entityPlayer;
        String playerName = player.getCommandSenderName();
        long currentTime = System.currentTimeMillis();
        
        // Перевіримо cooldown
        if (playerCooldownTime.containsKey(playerName)) {
            long lastAttackTime = playerCooldownTime.get(playerName);
            long cooldownMs = CombatConfig.GLOBAL_COOLDOWN * 50; // Convert ticks to ms
            
            if (currentTime - lastAttackTime < cooldownMs) {
                // В cooldown'і - блокуємо атаку
                event.setCanceled(true);
                return;
            }
        }
        
        // Встановлюємо час атаки
        playerCooldownTime.put(playerName, currentTime);
    }
    
    public static boolean isPlayerOnCooldown(EntityPlayer player) {
        String playerName = player.getCommandSenderName();
        long currentTime = System.currentTimeMillis();
        
        if (!playerCooldownTime.containsKey(playerName)) {
            return false;
        }
        
        long lastAttackTime = playerCooldownTime.get(playerName);
        long cooldownMs = CombatConfig.GLOBAL_COOLDOWN * 50;
        
        return (currentTime - lastAttackTime) < cooldownMs;
    }
    
    public static float getCooldownPercent(EntityPlayer player) {
        String playerName = player.getCommandSenderName();
        long currentTime = System.currentTimeMillis();
        
        if (!playerCooldownTime.containsKey(playerName)) {
            return 0.0f;
        }
        
        long lastAttackTime = playerCooldownTime.get(playerName);
        long cooldownMs = CombatConfig.GLOBAL_COOLDOWN * 50;
        long elapsedTime = currentTime - lastAttackTime;
        
        if (elapsedTime >= cooldownMs) {
            return 0.0f;
        }
        
        return (float) elapsedTime / cooldownMs;
    }
}
