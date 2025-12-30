package com.pierre.era_of_jujutsu.server;

import com.pierre.era_of_jujutsu.EraOfJujutsu;
import com.pierre.era_of_jujutsu.common.Util;
import com.pierre.era_of_jujutsu.common.command.EJCommand;
import com.pierre.era_of_jujutsu.common.register.EJCapabilities;
import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber()
public class ModBusEvent {

    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player player){
            JujutsuCapability cap = EJCapabilities.getJujutsuCap(player, JujutsuCapability.class);
            if(cap!=null){
                cap.init(player);
            }
        }
    }

    @SubscribeEvent
    public static void giveExp(PlayerXpEvent.XpChange event){
        Player player = event.getEntity();

        if (player.level().isClientSide) return;

        int xp = event.getAmount();
        int ceXp = Math.max(1, (int)(xp * 0.3));
        JujutsuCapability cap = JujutsuCapability.get(player);
        if(cap!=null){
            int currentLevel = cap.levelCE;
            if(currentLevel<10){
                cap.currentExp += ceXp;
                while (cap.currentExp>Util.getRequestExpForNextLevel(currentLevel)){
                    cap.uplevel();
                    currentLevel++;
                }
            }

        }

    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        JujutsuCapability cap = EJCapabilities.getJujutsuCap(event.getEntity(), JujutsuCapability.class);
        if(cap!=null){
            cap.tick();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            JujutsuCapability cap = EJCapabilities.getJujutsuCap(player, JujutsuCapability.class);
            if(cap==null){
                JujutsuCapability.JujutsuProvider provider = new JujutsuCapability.JujutsuProvider();
                JujutsuCapability newCap = provider.getCapability(EJCapabilities.JUJUTSU_CAPABILITY,null).orElse(null);
                newCap.init(player);
                event.addCapability(new ResourceLocation(EraOfJujutsu.MODID,"ce_capability"),provider);
            }
        }

    }
    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event){
        Player player=event.getOriginal();
        Player newPlayer=event.getEntity();
        player.reviveCaps();
        JujutsuCapability cap=JujutsuCapability.get(player);
        JujutsuCapability newCap=JujutsuCapability.get(newPlayer);
        newCap.clone(cap,newPlayer);
        player.invalidateCaps();
    }
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        EJCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        if(!event.getLevel().isClientSide && event.getItemStack().is(Items.STICK)){
            JujutsuCapability cap = JujutsuCapability.get(event.getEntity());
            if(cap!=null){
                if(cap.isSensibilityCE){
                    cap.uplevel();
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderEntity(RenderLivingEvent.Pre event){
        if(event.getEntity() instanceof Slime){
            event.setCanceled(!Util.canSeeCE(Minecraft.getInstance().player));
        }
    }
}
