package com.pierre.era_of_jujutsu.common;

public enum Grade {
    MONKEY,FOUR,THREE,TWO,ONE,SPECIAL,SPECIAL_PLUS;

    public static Grade getForID(int id){
        switch (id){
            case 0->{
                return FOUR;
            }
            case 1->{
                return THREE;
            }
            case 2->{
                return TWO;
            }
            case 3->{
                return ONE;
            }
            case 4->{
                return SPECIAL;
            }
            case 5->{
                return SPECIAL_PLUS;
            }
            default -> {
                return MONKEY;
            }
        }
    }
}
