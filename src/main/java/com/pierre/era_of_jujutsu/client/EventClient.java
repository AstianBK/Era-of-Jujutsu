package com.pierre.era_of_jujutsu.client;

import com.pierre.era_of_jujutsu.EraOfJujutsu;
import com.pierre.era_of_jujutsu.client.gui.CEBar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EraOfJujutsu.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class EventClient {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){
        /*event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new RegenerationLayer(event.getSkin(s)));
            event.getSkin(s).addLayer(new CastingLayer(event.getSkin(s)));
            event.getSkin(s).addLayer(new VampireLayer(event.getSkin(s)));
        });*/

    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //event.registerLayerDefinition(BloodSpikesModel.LAYER_LOCATION,BloodSpikesModel::createBodyLayer);

    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerGui(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "ce",new CEBar());
        //event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "skill_hotbar",new SkillOverlay());
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

    }

}
