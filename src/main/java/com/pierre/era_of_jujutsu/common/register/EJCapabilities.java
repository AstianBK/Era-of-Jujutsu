package com.pierre.era_of_jujutsu.common.register;

import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class EJCapabilities {
    public static final Capability<JujutsuCapability> JUJUTSU_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(JujutsuCapability.class);

        //event.register(AnimationPlayerCapability.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends JujutsuCapability> T getJujutsuCap(Entity entity, Class<T> type) {
        if (entity != null) {
            JujutsuCapability entitypatch = entity.getCapability(EJCapabilities.JUJUTSU_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }
}
