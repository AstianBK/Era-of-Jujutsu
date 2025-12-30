package com.pierre.era_of_jujutsu.server;

import com.pierre.era_of_jujutsu.common.register.EJAttribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.w3c.dom.ranges.Range;
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)

public class AttributeEvent {
    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, EJAttribute.CURSED_ENERGY);
    }
}
