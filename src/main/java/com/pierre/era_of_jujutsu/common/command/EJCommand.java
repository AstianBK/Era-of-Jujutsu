package com.pierre.era_of_jujutsu.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.pierre.era_of_jujutsu.EraOfJujutsu;
import com.pierre.era_of_jujutsu.common.Grade;
import com.pierre.era_of_jujutsu.common.Util;
import com.pierre.era_of_jujutsu.server.capabilities.JujutsuCapability;
import com.pierre.era_of_jujutsu.server.cursed_techniques.CEReinforcement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public class EJCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("jujutsu")
                        .requires(source -> source.hasPermission(2)).then(Commands.literal("sensibilityCE").then(
                                                Commands.argument("player", EntityArgument.player()).then(
                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                        .executes(ctx -> {
                                                                            ServerPlayer target =
                                                                                    EntityArgument.getPlayer(ctx, "player");
                                                                            boolean value =
                                                                                    BoolArgumentType.getBool(ctx, "value");

                                                                            JujutsuCapability cap = JujutsuCapability.get(target);

                                                                            if (cap != null) {
                                                                                boolean fueSensibility = cap.isSensibilityCE;
                                                                                cap.isSensibilityCE = value;
                                                                                if(fueSensibility!=value && value){
                                                                                    cap.levelCE = 1;
                                                                                    cap.grade = Grade.FOUR;
                                                                                    cap.maxCe = 100;
                                                                                    cap.hudXp.setVisible(true);
                                                                                    cap.hudXp.addPlayer(target);

                                                                                }
                                                                                if(!value){
                                                                                    cap.grade = Grade.MONKEY;
                                                                                    cap.maxCe = 0;
                                                                                    cap.hudXp.setVisible(false);
                                                                                    cap.hudXp.removeAllPlayers();
                                                                                }
                                                                                cap.isDirty = true;

                                                                            }
                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        ).requires(source -> source.hasPermission(2)).then(Commands.literal("setLevel").then(
                                        Commands.argument("player", EntityArgument.player()).then(
                                                Commands.argument("value", IntegerArgumentType.integer(1,10))
                                                        .executes(ctx -> {
                                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                                            int value = IntegerArgumentType.getInteger(ctx, "value");

                                                            JujutsuCapability cap = JujutsuCapability.get(target);

                                                            if (cap != null && cap.isSensibilityCE) {
                                                                cap.levelCE = value;
                                                                cap.currentExp = Util.getRequestExpForNextLevel(Math.max(value-1,0));
                                                                cap.maxCe = 100;
                                                                cap.isDirty = true;
                                                                if(cap.levelCE>5){
                                                                    EraOfJujutsu.LOGGER.debug("techniques pre "+cap.techniquesCE);
                                                                    cap.techniquesCE.put("ce_reinforcement",new CEReinforcement());
                                                                    EraOfJujutsu.LOGGER.debug("techniques post "+cap.techniquesCE);

                                                                }
                                                            }
                                                            return 1;
                                                        })
                                        )
                                )
                        )

        );
    }
}
