package com.pierre.era_of_jujutsu.server.capabilities;

import com.pierre.era_of_jujutsu.common.Grade;
import com.pierre.era_of_jujutsu.common.Util;
import com.pierre.era_of_jujutsu.common.network.PacketHandler;
import com.pierre.era_of_jujutsu.common.network.message.PacketSyncCapability;
import com.pierre.era_of_jujutsu.common.register.EJAttribute;
import com.pierre.era_of_jujutsu.common.register.EJCapabilities;
import com.pierre.era_of_jujutsu.common.register.EJTechniques;
import com.pierre.era_of_jujutsu.server.AttributeEvent;
import com.pierre.era_of_jujutsu.server.api.IJujutsu;
import com.pierre.era_of_jujutsu.server.cursed_techniques.CEReinforcement;
import com.pierre.era_of_jujutsu.server.cursed_techniques.CursedTechniques;
import com.pierre.era_of_jujutsu.server.cursed_techniques.TechniquesAbstract;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JujutsuCapability implements IJujutsu {
    private static final UUID ARMOR_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa111111111");
    private static final UUID SPEED_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa222222222");
    private static final UUID DAMAGE_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa333333333");
    private static final UUID TOUGHNESS_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa444444444");
    public Player player;
    public Level level;
    public boolean isSensibilityCE = false;
    public boolean firstJoin = false;
    public boolean isDirty = false;
    public Grade grade = Grade.MONKEY;
    public int levelCE = 0;
    public int maxCe = 0;
    public int currentExp = 0;
    public ServerBossEvent hudXp = new ServerBossEvent(Component.literal(grade.name()+"CE" ), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    public Map<String, CursedTechniques> techniquesCE = new HashMap<>();
    public Map<String, Integer> techniquesActive = new HashMap<>();
    public static JujutsuCapability get(Player player){
        return EJCapabilities.getJujutsuCap(player, JujutsuCapability.class);
    }
    @Override
    public int getCE() {
        return (int) player.getAttributeValue(EJAttribute.CURSED_ENERGY);
    }

    @Override
    public void setCE(float value) {
        player.getAttribute(EJAttribute.CURSED_ENERGY).setBaseValue(value);
    }

    @Override
    public void tick() {
        if(!level.isClientSide){
            if(this.isDirty){
                this.isDirty = false;
                PacketHandler.sendToPlayer(new PacketSyncCapability(this.serializeNBT()), (ServerPlayer) player);
            }
            if(this.isSensibilityCE){
                if(this.player.tickCount%100==0){
                    int ce = this.getCE();
                    if(ce<this.maxCe){
                        this.setCE(ce+getRegenerationCe());
                    }
                }
                if(!hudXp.getPlayers().contains((ServerPlayer) player)){
                    hudXp.addPlayer((ServerPlayer) player);
                }
                float exp = this.levelCE>1 ? (float) this.currentExp-(float) Util.getRequestExpForNextLevel(levelCE-1) : this.currentExp;
                float nextExp = this.levelCE>1 ? (float) Util.getRequestExpForNextLevel(levelCE) -(float) Util.getRequestExpForNextLevel(levelCE-1) : (float) Util.getRequestExpForNextLevel(levelCE) ;
                hudXp.setProgress(exp/nextExp);
                hudXp.setName(Component.literal("Level :"+levelCE+" Experience :"+this.currentExp));
            }
        }
    }

    public boolean canSeeCE(){
        return this.isSensibilityCE && this.levelCE>=2;
    }
    public float getRegenerationCe(){
        return Util.getRegenerationBaseForGrade(grade) + 0.5F*this.levelCE;
    }

    public void clone(JujutsuCapability old,Player player){
        this.isDirty = true;
        this.grade = old.grade;
        this.levelCE = old.levelCE;
        this.isSensibilityCE = old.isSensibilityCE;
        this.player = player;
        this.level = player.level();
        this.currentExp = old.currentExp;
        this.maxCe = old.maxCe;
        this.firstJoin = old.firstJoin;

        old.hudXp.setVisible(false);
        old.hudXp.setProgress(0);
        old.hudXp.removeAllPlayers();
        if(!player.level().isClientSide){
            this.modifyStatForLevel();
        }
    }

    public void init(Player player){
        this.player = player;
        this.level = player.level();
        if(!level.isClientSide() && !this.firstJoin){
            this.firstJoin=true;
            this.isSensibilityCE=level.random.nextBoolean();
            if(this.isSensibilityCE){
                this.grade = Grade.FOUR;
                this.levelCE = 1;
                this.maxCe = 100;
                this.currentExp = 0;
            }
        }
        this.isDirty = true;
    }
    public void uplevel(){
        this.levelCE=Math.min(10,this.levelCE+1);
        if (levelCE%2==0){
            this.modifyStatForLevel();
        }
        if(levelCE>5){
            this.techniquesCE.put("ce_reinforcement",new CEReinforcement());
        }
        this.isDirty = true;
    }

    private void modifyStatForLevel() {
        int modifier = this.levelCE/2;
        applyModifier(Attributes.ARMOR, ARMOR_MOD_UUID,
                "ce_armor", modifier*0.5F, AttributeModifier.Operation.ADDITION);

        applyModifier(Attributes.MOVEMENT_SPEED, SPEED_MOD_UUID,
                "ce_speed", modifier * 0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        applyModifier(Attributes.ATTACK_DAMAGE, DAMAGE_MOD_UUID,
                "ce_damage", modifier, AttributeModifier.Operation.ADDITION);

    }

    private void applyModifier(Attribute attribute, UUID uuid, String name, double value, AttributeModifier.Operation operation) {
        var attr = player.getAttribute(attribute);
        if (attr == null) return;

        if (attr.getModifier(uuid) != null) {
            attr.removeModifier(uuid);
        }

        attr.addPermanentModifier(new AttributeModifier(uuid, name, value, operation));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("firstJoin",this.firstJoin);
        if(this.isSensibilityCE){
            nbt.putBoolean("isSensibility",true);
            nbt.putString("grade",this.grade.name());
            nbt.putInt("level",this.levelCE);
            nbt.putInt("currentExp",this.currentExp);
            nbt.putInt("maxCe",this.maxCe);
            ListTag list = new ListTag();
            this.techniquesCE.forEach((key,value) ->{
                CompoundTag tag = new CompoundTag();
                tag.putString("name",key);
                list.add(tag);
            });
            nbt.put("techniques",list);
        }
        return nbt;
    }


    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.firstJoin = nbt.getBoolean("firstJoin");
        if(nbt.contains("isSensibility")){
            this.isSensibilityCE = nbt.getBoolean("isSensibility");
            this.grade = Grade.valueOf(nbt.getString("grade"));
            this.levelCE = nbt.getInt("level");
            this.currentExp = nbt.getInt("currentExp");
            this.maxCe = nbt.getInt("maxCe");
            if(nbt.contains("techniques")){
                Map<String,CursedTechniques> map = new HashMap<>();
                ListTag list = nbt.getList("techniques",10);
                for (int i = 0 ; i<list.size() ; i++){
                    String id = list.getCompound(i).getString("name");
                    map.put(id, (CursedTechniques) EJTechniques.getTechniqueForName(id));
                }
                this.techniquesCE = map;
            }
        }else {
            this.grade = Grade.MONKEY;
            this.isSensibilityCE = false;
        }
        this.isDirty = true;
    }
    
    public static class JujutsuProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        private final LazyOptional<IJujutsu> instance = LazyOptional.of(JujutsuCapability::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return EJCapabilities.JUJUTSU_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
