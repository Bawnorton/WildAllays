package com.bawnorton.wildallays.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;

public class CommandHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> ParticleCommand.register(dispatcher)));
    }

    public static class ParticleCommand {
        public static double radius = 1;
        public static double circleTop = 0;
        public static double circleAmplitude = 0.5;
        public static double helixTop = 1;
        public static double helixAmplitude = 1;
        public static double circleSpeed = 5;
        public static double yAxisRot = 0;
        public static double angle = 0;
        public static boolean doubleHelix = false;


        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(CommandManager.literal("wildallays_particle").requires((source) -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("radius").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        radius = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("radius set to %s".formatted(radius)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("circleTop").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        circleTop = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("circleTop set to %s".formatted(circleTop)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("circleAmplitude").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        circleAmplitude = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("circleAmplitude set to %s".formatted(circleAmplitude)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("helixTop").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        helixTop = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("helixTop set to %s".formatted(helixTop)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("helixAmplitude").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        helixAmplitude = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("helixAmplitude set to %s".formatted(helixAmplitude)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("circleSpeed").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        circleSpeed = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("circleSpeed set to %s".formatted(circleSpeed)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("yAxisRot").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        yAxisRot = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("yAxisRot set to %s".formatted(yAxisRot)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("angle").then(argument("value", DoubleArgumentType.doubleArg()).executes((context -> {
                        angle = DoubleArgumentType.getDouble(context, "value");
                        context.getSource().sendFeedback(new LiteralText("angle set to %s".formatted(angle)), false);
                        return 1;
                    }))))
                    .then(CommandManager.literal("doubleHelix").then(argument("value", BoolArgumentType.bool()).executes((context -> {
                        doubleHelix = BoolArgumentType.getBool(context, "value");
                        context.getSource().sendFeedback(new LiteralText("doubleHelix set to %s".formatted(doubleHelix)), false);
                        return 1;
                    }))))
            );
        }
    }
}
