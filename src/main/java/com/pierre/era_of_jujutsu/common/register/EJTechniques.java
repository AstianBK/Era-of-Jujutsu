package com.pierre.era_of_jujutsu.common.register;

import com.pierre.era_of_jujutsu.server.cursed_techniques.CEReinforcement;
import com.pierre.era_of_jujutsu.server.cursed_techniques.CursedTechniques;
import com.pierre.era_of_jujutsu.server.cursed_techniques.TechniquesAbstract;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EJTechniques {
    public static Map<String, Supplier<TechniquesAbstract>> curseTechniques = new HashMap<>();

    public static void registerTechniques(String name , Supplier<TechniquesAbstract> technique){
        curseTechniques.put(name,technique);
    }

    public static void initialize(){
        registerTechniques("none",()-> new CursedTechniques("none",0));
        registerTechniques("ce_reinforcement", CEReinforcement::new);
    }
    public static TechniquesAbstract getTechniqueForName(String name){
        return curseTechniques.getOrDefault(name,null).get();
    }
}
