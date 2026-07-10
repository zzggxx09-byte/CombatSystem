package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class CombatEventHandler {
    
    private static Map<String, Integer> playerAttackCooldown = new HashMap<>();
    
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (!CombatConfig.COMBAT_SYSTEM_ENABLED) {
            return;
        }
        
        EntityPlayer player = event.entityPlayer;
        String playerName = player.getCommandSenderName();
        
        if (isOnCooldown(playerName)) {
            event.setCanceled(true);
            return;
        }
        
        int cooldown = getCooldownForCurrentTool(player);
        setCooldown(playerName, cooldown);
        
        if (CombatConfig.ITEM_ANIMATION_ENABLED) {
            activateItemAnimation(player);
        }
        
        showCooldownIndicator(player);
    }
    
    private boolean isOnCooldown(String playerName) {
        if (!playerAttackCooldown.containsKey(playerName)) {
            return false;
        }
        
        int remainingCooldown = playerAttackCooldown.get(playerName) - 1;
        
        if (remainingCooldown <= 0) {
            playerAttackCooldown.remove(playerName);
            return false;
        }
        
        playerAttackCooldown.put(playerName, remainingCooldown);
        return true;
    }
    
    private void setCooldown(String playerName, int ticks) {
        playerAttackCooldown.put(playerName, ticks);
    }
    
    private int getCooldownForCurrentTool(EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        
        if (heldItem == null) {
            return CombatConfig.getCooldownForTool("bare_hand");
        }
        
        String toolName = heldItem.getItem().getUnlocalizedName();
        
        if (toolName.startsWith("item.")) {
            toolName = toolName.substring(5);
        }
        
        return CombatConfig.getCooldownForTool(toolName);
    }
    
    private void activateItemAnimation(EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        
        if (heldItem == null) {
            return;
        }
        
        if (!heldItem.hasTagCompound()) {
            heldItem.setTagCompound(new net.minecraft.nbt.NBTTagCompound());
        }
        
        heldItem.getTagCompound().setBoolean("IsAttacking", true);
        heldItem.getTagCompound().setInteger("AttackTick", 0);
    }
    
    private void showCooldownIndicator(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return;
        }
    }
    
    public static float getRemainingCooldownPercent(EntityPlayer player) {
        String playerName = player.getCommandSenderName();
        
        // Для 1.7.10 використовуємо capabilities.isCreativeMode
        if (!player.capabilities.isCreativeMode && CombatConfig.COMBAT_SYSTEM_ENABLED) {
            if (playerAttackCooldown.containsKey(playerName)) {
                int remaining = playerAttackCooldown.get(playerName);
                int cooldown = CombatConfig.getCooldownForTool("default");
                return (float) remaining / cooldown;
            }
        }
        
        return 0.0f;
    }
}
