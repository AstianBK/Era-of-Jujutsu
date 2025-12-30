package com.pierre.era_of_jujutsu.common.network.message;

import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCapability {
    private final CompoundTag tag;
    public PacketSyncCapability(CompoundTag tag){
        this.tag = tag;
    }
    public PacketSyncCapability(FriendlyByteBuf buf){
        this.tag = buf.readNbt();
    }

    public void write(FriendlyByteBuf buf){
        buf.writeNbt(tag);
    }

    public void handler(Supplier<NetworkEvent.Context> supplier){
        assert supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
        JujutsuCapability cap = JujutsuCapability.get(Minecraft.getInstance().player);
        if(cap!=null){
            cap.deserializeNBT(tag);
            cap.isDirty = false;
        }
    }
}
