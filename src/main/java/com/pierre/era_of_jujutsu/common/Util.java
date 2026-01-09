package com.pierre.era_of_jujutsu.common;

import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import net.minecraft.world.entity.player.Player;

public class Util {
    public static boolean isSensibilityCE(Player player){
        JujutsuCapability cap = JujutsuCapability.get(player);
        return cap!=null && cap.isSensibilityCE;
    }
    public static boolean isDamageCE(Player player){
        JujutsuCapability cap = JujutsuCapability.get(player);
        return cap!=null && cap.isSensibilityCE;
    }
    public static boolean canSeeCE(Player player){
        JujutsuCapability cap = JujutsuCapability.get(player);
        return cap!=null && cap.canSeeCE();
    }
    public static double getMaxCE(Player player){
        JujutsuCapability cap = JujutsuCapability.get(player);
        if(cap!=null){
            return cap.maxCe;
        }
        return 1;
    }
    public static int getRequestExpForNextLevel(int levelCE){
        switch (levelCE){
            case 1 ->{
                return 100;
            }
            case 2->{
                return 280;
            }
            case 3->{
                return 580;
            }
            case 4 ->{
                return 1030;
            }
            case 5 ->{
                return 1680;
            }
            case 6 ->{
                return 2580;
            }
            case 7 ->{
                return 3780;
            }
            case 8 ->{
                return 5380;
            }
            case 9, 10 ->{
                return 7580;
            }
            default -> {
                return 0;
            }
        }
    }
    public static int getRegenerationBaseForGrade(Grade grade){
        switch (grade){
            case FOUR -> {
                return 1;
            }
            case THREE -> {
                return 2;
            }
            case TWO -> {
                return 3;
            }
            case ONE -> {
                return 4;
            }
            case SPECIAL -> {
                return 5;
            }
            case SPECIAL_PLUS -> {
                return 6;
            }
            default -> {
                return 0;
            }
        }
    }
}
