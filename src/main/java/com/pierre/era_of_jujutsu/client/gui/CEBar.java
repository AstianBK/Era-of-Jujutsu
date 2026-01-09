package com.pierre.era_of_jujutsu.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.pierre.era_of_jujutsu.EraOfJujutsu;
import com.pierre.era_of_jujutsu.common.Util;
import com.pierre.era_of_jujutsu.common.register.EJAttribute;
import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import com.pierre.era_of_jujutsu.server.cursed_techniques.CursedTechniques;
import com.pierre.era_of_jujutsu.server.cursed_techniques.TechniquesAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.*;
import java.util.Map;

public class CEBar implements IGuiOverlay {
    public static final ResourceLocation BAR = new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/ce_bar_full.png");
    public static final ResourceLocation BAR_EXP = new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/xp_bar.png");
    public static final ResourceLocation EMPTY = new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/ce_bar_empty.png");
    public static final ResourceLocation[] TECHNIQUES_MARC = new ResourceLocation[]{
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_top.png"),
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_middle.png"),
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_bot.png")
    };
    public static final ResourceLocation[] ICONS = new ResourceLocation[]{
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_heart.png"),
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_armor.png"),
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_eat.png"),
            new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/icon_bubble.png")
    };
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if(player==null || player.isCreative())return;
        if(Util.isSensibilityCE(player)){
            guiGraphics.pose().pushPose();
            double ce = player.getAttributeBaseValue(EJAttribute.CURSED_ENERGY_VALUE.get());
            double max = Util.getMaxCE(player);
            int x = (int) (screenWidth/2.0F);
            int y = screenHeight - ((ForgeGui)Minecraft.getInstance().gui).leftHeight;
            //y=30 es la altura base de la vacia ,y=15 la full
            for (int i = 0 ; i < 4 ; i++){
                int xIcon = x + 5 + i * 25;
                guiGraphics.blit(ICONS[i], xIcon, y-8,0,0,9,9,9,9);
                int value = getValueForIndex(i,player);
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(value), xIcon+18, y-8,10526880);
            }

            guiGraphics.blit(BAR_EXP, x-87, y-3,0,0,174,19,360,128);

            guiGraphics.blit(EMPTY, x-91, y - 20,0,24,64,24,64,24);
            guiGraphics.blit(BAR, x-79, y - 5,12,15,Mth.ceil((ce/max)*50.0F),5, 64,24);
            JujutsuCapability cap = JujutsuCapability.get(player);
            if(!cap.techniquesCE.isEmpty()){
                int index = 0;
                for (int i=-1 ; i < 2 ; i++){
                    int indexFinal = index-i;
                    if(indexFinal == -1){
                        indexFinal = cap.techniquesCE.size()-1;
                    }
                    if (indexFinal == cap.techniquesCE.size()){
                        indexFinal = 0;
                    }

                    CursedTechniques cursed = cap.techniquesCE.values().stream().findFirst().get();

                    guiGraphics.pose().pushPose();
                    int xFinal = x- 140;
                    int yFinal = y - 38 + 38*i;
                    int xIcon = x-129;
                    int yIcon = yFinal + 12 - (i+1)*2;

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableDepthTest();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);

                    if(i!=0){
                        RenderSystem.setShaderColor(0.6F, 0.6F, 0.6F, 0.4F);
                    }
                    guiGraphics.blit(TECHNIQUES_MARC[i + 1], xFinal, yFinal, 0, 0, 38, 36, 38, 36);

                    guiGraphics.blit(getIconsForSkill(cursed), xIcon, yIcon, 0, 0, 16, 16, 16, 16);

                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableDepthTest();
                    RenderSystem.disableBlend();
                    guiGraphics.pose().popPose();
                }

                for (int i=-1 ; i < 2 ; i++){
                    int indexFinal = index-i;
                    if(indexFinal == -1){
                        indexFinal = cap.techniquesCE.size()-1;
                    }
                    if (indexFinal == cap.techniquesCE.size()){
                        indexFinal = 0;
                    }

                    CursedTechniques cursed = cap.techniquesCE.values().stream().findFirst().get();
                    int xFinal = x+100;
                    int yFinal = y - 38 + 38*i;
                    guiGraphics.pose().pushPose();

                    int xIcon = x+111;
                    int yIcon = yFinal + 12 - (i+1)*2;
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableDepthTest();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    if(i!=0){
                        RenderSystem.setShaderColor(0.6F, 0.6F, 0.6F, 0.4F);
                    }
                    guiGraphics.blit(TECHNIQUES_MARC[i+1], xFinal, yFinal, 0,0,38, 36, 38, 36);

                    guiGraphics.blit(getIconsForSkill(cursed), xIcon, yIcon, 0,0,16, 16, 16, 16);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableDepthTest();
                    RenderSystem.disableBlend();
                    guiGraphics.pose().popPose();
                }
            }
            guiGraphics.pose().popPose();
        }
    }

    private int getValueForIndex(int i,Player player) {
        switch (i){
            case 0 :{
                return (int) player.getHealth();
            }
            case 1 :{
                return player.getArmorValue();
            }
            case 2:{
                return player.getFoodData().getFoodLevel();
            }
            case 3:{
                return player.getAirSupply();
            }
            default:return 0;
        }
    }

    private ResourceLocation getIconsForSkill(TechniquesAbstract skillAbstract) {
        return new ResourceLocation(EraOfJujutsu.MODID,"textures/gui/techniques/"+skillAbstract.name+"_icon.png");
    }
}
