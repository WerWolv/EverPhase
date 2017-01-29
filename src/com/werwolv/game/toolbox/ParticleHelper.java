package com.werwolv.game.toolbox;

import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.entity.particle.EntityParticle;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.render.RendererParticle;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleHelper {

    private static List<EntityParticle> particles = new ArrayList<>();
    private static RendererParticle renderer;

    public static void init(ResourceLoader loader, Matrix4f projectionMatrix) {
        renderer = new RendererParticle(loader, projectionMatrix);
    }

    public static void update() {
        Iterator<EntityParticle> iter = particles.iterator();

        while(iter.hasNext()) {
            EntityParticle p = iter.next();
            if(!p.update())
                iter.remove();
        }
    }

    public static void renderParticles(EntityPlayer player) {
        renderer.render(particles, player);
    }

    public static void clean() {
        renderer.clean();
        particles.clear();
    }

    public static void addParticle(EntityParticle particle) {
        particles.add(particle);
    }

}
