package com.pierre.era_of_jujutsu.server.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IJujutsu extends INBTSerializable<CompoundTag> {
    int getCE();
    void setCE(float value);
    void tick();

}
