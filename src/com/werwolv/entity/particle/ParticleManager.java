package com.werwolv.entity.particle;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.RendererParticle;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager {

    private static List<EntityParticle> particles = new ArrayList<>();
    private static RendererParticle rendererParticle;

    public static void init(ModelLoader loader, Matrix4f projectionMatrix) {
        rendererParticle = new RendererParticle(loader, projectionMatrix);
    }

    public static void updateParticles() {
        Iterator<EntityParticle> iterator = particles.iterator();

        while (iterator.hasNext()) {
            EntityParticle p = iterator.next();

            boolean stillAlive = p.update();

            if (!stillAlive) iterator.remove();
        }
    }

    public static void renderParticles(EntityPlayer player) {
        rendererParticle.render(particles, player);
    }

    public static void addParticle(EntityParticle particle) {
        particles.add(particle);
    }

    public static void clean() {
        rendererParticle.clean();
    }

}
