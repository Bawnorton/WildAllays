package com.bawnorton.wildallays.particle;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;

public class ParticleHelixGenerator {
    private final ClientWorld world;
    private final ParticleEffect effect;
    private final double particles;
    private double step = 0;


    public ParticleHelixGenerator(ClientWorld world, ParticleEffect effect, double particles) {
        this.world = world;
        this.effect = effect;
        this.particles = particles;
    }

    public void increment() {
        step++;
        if(step >= particles) step = 0;
    }

    public void create(double radius, double circleTop, double circleAmplitude, double helixTop, double helixAmplitude, double circleSpeed, double centerX, double centerY, double centerZ, double yAxisRot, double angle, boolean doubleHelix) {
        for(int j = 1; j > -2; j -= 2) {
            double r = radius + Math.round(circleAmplitude * Math.sin(circleTop * circleSpeed * 2 * Math.PI * (step / particles)) * 1000) / 1000D;
            double p = circleSpeed * 2 * Math.PI * ((((yAxisRot / 360) * particles) + step) / particles);
            double t = (angle / 45) * 0.25 * Math.PI * Math.sin(circleSpeed * 2 * Math.PI * (step / particles));
            double x = centerX + Math.round((r * Math.cos(p + j * Math.PI) * Math.cos(t)) * 1000) / 1000D * j;
            double y = centerY + Math.round((r * Math.sin(t) + helixAmplitude * Math.sin(helixTop * 2 * Math.PI * (step / particles))) * 1000) / 1000D;
            double z = centerZ + Math.round((r * Math.sin(p + j * Math.PI) * Math.cos(t)) * 1000) / 1000D * j;
            world.addParticle(effect, x, y, z, 0, 0, 0);
            if(!doubleHelix) break;
        }
    }
}
