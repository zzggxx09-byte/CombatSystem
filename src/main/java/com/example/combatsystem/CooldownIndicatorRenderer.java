package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CooldownIndicatorRenderer {
    
    // Текстури
    private static final ResourceLocation READY_TEXTURE = new ResourceLocation("combatsystem:textures/crosshair_ready.png");
    private static final ResourceLocation COOLDOWN_TEXTURE = new ResourceLocation("combatsystem:textures/crosshair_cooldown.png");
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }
        
        if (!CombatConfig.COMBAT_SYSTEM_ENABLED) {
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        
        if (player == null) {
            return;
        }
        
        // Перевіримо чи гравець наводить на моба
        EntityLivingBase target = getTargetEntity(player);
        
        if (target != null) {
            // Є мобу - рисуємо текстуру
            boolean onCooldown = CombatEventHandler.isPlayerOnCooldown(player);
            
            int screenWidth = event.resolution.getScaledWidth();
            int screenHeight = event.resolution.getScaledHeight();
            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;
            
            drawCrosshairTexture(mc, centerX, centerY, onCooldown);
        }
    }
    
    private void drawCrosshairTexture(Minecraft mc, int x, int y, boolean onCooldown) {
        TextureManager textureManager = mc.getTextureManager();
        
        // Вибираємо текстуру залежно від cooldown'у
        ResourceLocation texture = onCooldown ? COOLDOWN_TEXTURE : READY_TEXTURE;
        
        textureManager.bindTexture(texture);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Рисуємо маленьке зображення (16x16 пікселів)
        int size = 16;
        drawTexturedRect(x - size/2, y - size/2, size, size, 0, 0, 1, 1);
        
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    private void drawTexturedRect(int x, int y, int width, int height, float minU, float minV, float maxU, float maxV) {
        float f = 1.0F / 256.0F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(minU * f, maxV * f);
        GL11.glVertex2f((float)(x + 0), (float)(y + height));
        GL11.glTexCoord2f(maxU * f, maxV * f);
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glTexCoord2f(maxU * f, minV * f);
        GL11.glVertex2f((float)(x + width), (float)(y + 0));
        GL11.glTexCoord2f(minU * f, minV * f);
        GL11.glVertex2f((float)(x + 0), (float)(y + 0));
        GL11.glEnd();
    }
    
    private EntityLivingBase getTargetEntity(EntityPlayer player) {
        double distance = 5.0D;
        EntityLivingBase target = null;
        double closestDistance = distance;
        
        for (Object obj : player.worldObj.getLoadedEntityList()) {
            if (obj instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) obj;
                
                if (entity != player && entity.isEntityAlive()) {
                    double dist = entity.getDistance(player.posX, player.posY, player.posZ);
                    
                    if (dist < closestDistance) {
                        closestDistance = dist;
                        target = entity;
                    }
                }
            }
        }
        
        return target;
    }
}
