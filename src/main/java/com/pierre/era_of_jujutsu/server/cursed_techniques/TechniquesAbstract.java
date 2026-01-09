package com.pierre.era_of_jujutsu.server.cursed_techniques;

import net.minecraft.world.entity.player.Player;

public abstract class TechniquesAbstract {
    public final String name;
    public TechniquesAbstract(String name){
        this.name = name;
    }

    public void tick(Player player){

    }
    public String getName() {
        return this.name;
    }

    public void handlerTechnique(Player player){

    }


    public void stopTechnique(Player player){

    }
}
