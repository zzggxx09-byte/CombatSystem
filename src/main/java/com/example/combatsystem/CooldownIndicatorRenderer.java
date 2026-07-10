package com.example.combatsystem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class CooldownIndicatorRenderer {
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }
        
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        
        if (player == null) {
            return;
        }
        
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        boolean canAttack = CombatEventHandler.canAttack(player);
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        if (canAttack) {
            // ЗЕЛЕНА - готово
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            drawSquare(centerX - 3, centerY + 10, 6, 6);
        } else {
            // ЧЕРВОНА - cooldown
            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            drawSquare(centerX - 3, centerY + 10, 6, 6);
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
    private void drawSquare(int x, int y, int w, int h) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x + w, y);
        GL11.glVertex2i(x + w, y + h);
        GL11.glVertex2i(x, y + h);
        GL11.glEnd();
    }
}
