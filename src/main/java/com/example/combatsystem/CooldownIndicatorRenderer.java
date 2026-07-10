package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class CooldownIndicatorRenderer {
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
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
        
        Entity pointedEntity = getPointedEntity(player);
        
        if (pointedEntity != null) {
            float cooldownPercent = CombatEventHandler.getRemainingCooldownPercent(player);
            
            int screenWidth = event.resolution.getScaledWidth();
            int screenHeight = event.resolution.getScaledHeight();
            
            drawCooldownIndicator(mc, screenWidth, screenHeight, cooldownPercent);
        }
    }
    
    private void drawCooldownIndicator(Minecraft mc, int screenWidth, int screenHeight, float cooldown) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        
        int radius = 15;
        int thickness = 2;
        
        if (cooldown > 0.0f) {
            GL11.glColor4f(1.0f, 0.5f, 0.0f, 0.8f);
            
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            
            int segments = (int) (360 * cooldown);
            for (int i = 0; i <= segments; i++) {
                float angle = (i / 360.0f) * (float) Math.PI * 2.0f;
                
                float x1 = (float) Math.cos(angle) * radius;
                float y1 = (float) Math.sin(angle) * radius;
                
                float x2 = (float) Math.cos(angle) * (radius - thickness);
                float y2 = (float) Math.sin(angle) * (radius - thickness);
                
                GL11.glVertex2f(centerX + x1, centerY + y1);
                GL11.glVertex2f(centerX + x2, centerY + y2);
            }
            
            GL11.glEnd();
        } else {
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
            
            GL11.glBegin(GL11.GL_LINE_LOOP);
            int segments = 32;
            for (int i = 0; i < segments; i++) {
                float angle = (i / (float) segments) * (float) Math.PI * 2.0f;
                float x = (float) Math.cos(angle) * radius;
                float y = (float) Math.sin(angle) * radius;
                GL11.glVertex2f(centerX + x, centerY + y);
            }
            GL11.glEnd();
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }
    
    private Entity getPointedEntity(EntityPlayer player) {
        double distance = 5.0D;
        Entity pointedEntity = null;
        
        double closestDistance = Double.MAX_VALUE;
        
        for (Object obj : player.worldObj.getLoadedEntityList()) {
            Entity entity = (Entity) obj;
            
            if (entity != player && !entity.isInvisible()) {
                double entityDistance = entity.getDistance(player.posX, player.posY, player.posZ);
                
                if (entityDistance < distance && entityDistance < closestDistance) {
                    closestDistance = entityDistance;
                    pointedEntity = entity;
                }
            }
        }
        
        return pointedEntity;
    }
}
