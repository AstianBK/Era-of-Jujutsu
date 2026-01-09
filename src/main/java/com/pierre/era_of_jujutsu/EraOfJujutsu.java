package com.pierre.era_of_jujutsu;

import com.mojang.logging.LogUtils;
import com.pierre.era_of_jujutsu.common.command.EJCommand;
import com.pierre.era_of_jujutsu.common.network.PacketHandler;
import com.pierre.era_of_jujutsu.common.register.EJAttribute;
import com.pierre.era_of_jujutsu.common.register.EJCapabilities;
import com.pierre.era_of_jujutsu.common.register.EJTechniques;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(EraOfJujutsu.MODID)
public class EraOfJujutsu
{
    public static final String MODID = "era_of_jujutsu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EraOfJujutsu() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        EJTechniques.initialize();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(EJCapabilities::registerCapabilities);
        PacketHandler.registerMessages();
        EJAttribute.ATTRIBUTES.register(modEventBus);
    }


}
