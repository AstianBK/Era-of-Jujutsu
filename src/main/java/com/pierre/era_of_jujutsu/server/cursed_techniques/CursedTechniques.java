package com.pierre.era_of_jujutsu.server.cursed_techniques;

public class CursedTechniques extends TechniquesAbstract{
    public final int cost;
    public CursedTechniques(String name, int cost) {
        super(name);
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }
}
