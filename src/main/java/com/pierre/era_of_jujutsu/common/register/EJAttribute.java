package com.pierre.era_of_jujutsu.common.register;

import com.pierre.era_of_jujutsu.EraOfJujutsu;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EJAttribute {
    public static Attribute CURSED_ENERGY = new RangedAttribute("ce",0.0F,0.0F,Double.MAX_VALUE).setSyncable(true);

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, EraOfJujutsu.MODID);
    public static RegistryObject<Attribute> CURSED_ENERGY_VALUE= ATTRIBUTES.register("ce",()-> CURSED_ENERGY);

}
