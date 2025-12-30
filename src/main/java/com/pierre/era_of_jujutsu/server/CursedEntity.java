package com.pierre.era_of_jujutsu.server;

import com.pierre.era_of_jujutsu.common.Grade;
import com.pierre.era_of_jujutsu.common.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CursedEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_GRADE = SynchedEntityData.defineId(CursedEntity.class, EntityDataSerializers.INT);

    protected CursedEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GRADE,0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("grade",this.getGradeId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setGradeId(p_21450_.getInt("grade"));
    }

    public void setGradeId(int id){
        this.entityData.set(DATA_GRADE,id);
    }

    public int getGradeId(){
        return this.entityData.get(DATA_GRADE);
    }
    public Grade getGradeForId(){
        return Grade.getForID(this.getGradeId());
    }
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if(p_21016_.getEntity() instanceof Player player){
            if(Util.isDamageCE(player)){
                return super.hurt(p_21016_, p_21017_);
            }
        }
        return false;
    }
}
