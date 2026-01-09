package com.pierre.era_of_jujutsu.server.cursed_techniques;

import com.pierre.era_of_jujutsu.common.register.EJAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CEReinforcement extends CursedTechniques{
    private static final UUID ARMOR_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa111111112");
    private static final UUID SPEED_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa222222223");
    private static final UUID DAMAGE_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa333333334");
    private static final UUID TOUGHNESS_MOD_UUID =
            UUID.fromString("7a1b9c4e-1a1a-4b99-9c61-aaa444444445");
    public final Map<Attribute,Float> attributes;
    public CEReinforcement() {
        super("ce_reinforcement", 1);
        Map<Attribute,Float> map = new HashMap<>();
        map.put(Attributes.ATTACK_DAMAGE,5.0f);
        map.put(Attributes.ARMOR,2.0F);
        map.put(Attributes.MOVEMENT_SPEED,0.1F);
        this.attributes = map;
    }

    @Override
    public void tick(Player player) {
        super.tick(player);
        float ce = (float) player.getAttributeValue(EJAttribute.CURSED_ENERGY_VALUE.get());
        if(player.tickCount%20==0){
            if(ce>cost){
                player.getAttribute(EJAttribute.CURSED_ENERGY_VALUE.get()).setBaseValue(ce-1);
            }else {
                stopTechnique(player);
            }
        }

    }

    @Override
    public void handlerTechnique(Player player) {
        super.handlerTechnique(player);
        modifyStat(player,true);
    }

    @Override
    public void stopTechnique(Player player) {
        super.stopTechnique(player);
        modifyStat(player,false);
    }

    private void modifyStat(Player player, boolean active) {
        for (Map.Entry<Attribute,Float> entry : attributes.entrySet()){
            UUID uuid = getUUIDForAttribute(entry.getKey());
            if(uuid==null)continue;
            if(active){
                float modifier = entry.getValue();
                applyModifier(player,entry.getKey(),uuid,
                        "ce_reinforcement_"+entry.getKey().getDescriptionId().split("\\.")[3], modifier*0.5F, AttributeModifier.Operation.ADDITION);
            }else {
                removeModifier(player,entry.getKey(),uuid);
            }
        }
    }

    public UUID getUUIDForAttribute(Attribute attribute){
        switch (attribute.getDescriptionId()){
            case "attribute.name.generic.armor" ->{
                return ARMOR_MOD_UUID;
            }
            case "attribute.name.generic.movement_speed" ->{
                return SPEED_MOD_UUID;
            }
            case "attribute.name.generic.attack_damage" ->{
                return DAMAGE_MOD_UUID;
            }
            case "attribute.name.generic.armor_toughness" ->{
                return TOUGHNESS_MOD_UUID;
            }
        }
        return null;
    }

    private void applyModifier(Player player, Attribute attribute, UUID uuid, String name, double value, AttributeModifier.Operation operation) {
        var attr = player.getAttribute(attribute);
        if (attr == null) return;

        if (attr.getModifier(uuid) != null) {
            attr.removeModifier(uuid);
        }

        attr.addPermanentModifier(new AttributeModifier(uuid, name, value, operation));
    }
    private void removeModifier(Player player, Attribute attribute, UUID uuid) {
        var attr = player.getAttribute(attribute);
        if (attr == null) return;

        if (attr.getModifier(uuid) != null) {
            attr.removeModifier(uuid);
        }
    }
}
