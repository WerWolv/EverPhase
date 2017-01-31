package com.deltabase.everphase.engine.toolbox;

import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.engine.render.RendererParticle;
import com.deltabase.everphase.engine.resource.TextureParticle;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.entity.particle.EntityParticle;
import org.joml.Matrix4f;

import java.util.*;

public class ParticleHelper {

    private static Map<TextureParticle, List<EntityParticle>> particles = new HashMap<>();
    private static RendererParticle renderer;

    public static void init(ResourceLoader loader, Matrix4f projectionMatrix) {
        renderer = new RendererParticle(loader, projectionMatrix);
    }

    public static void update(EntityPlayer player) {
        Iterator<Map.Entry<TextureParticle, List<EntityParticle>>> mapIterator = particles.entrySet().iterator();

        while(mapIterator.hasNext()) {
            Map.Entry<TextureParticle, List<EntityParticle>> entry = mapIterator.next();
            List<EntityParticle> list = entry.getValue();
            Iterator<EntityParticle> iter = list.iterator();

            while(iter.hasNext()) {
                EntityParticle p = iter.next();
                if(!p.update(player)) {
                    iter.remove();

                    if(list.isEmpty())
                        mapIterator.remove();
                }
            }
            if(!entry.getKey().usesAdditiveBlending())
                sortHighToLow(list);
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
        List<EntityParticle> list = particles.computeIfAbsent(particle.getTexture(), k -> new ArrayList<>());

        list.add(particle);
    }


    private static void sortHighToLow(List<EntityParticle> list) {
        for (int i = 1; i < list.size(); i++) {
            EntityParticle item = list.get(i);
            if (item.getDistanceToCamera() > list.get(i - 1).getDistanceToCamera()) {
                sortUpHighToLow(list, i);
            }
        }
    }

    private static void sortUpHighToLow(List<EntityParticle> list, int i) {
        EntityParticle item = list.get(i);
        int attemptPos = i - 1;
        while (attemptPos != 0 && list.get(attemptPos - 1).getDistanceToCamera() < item.getDistanceToCamera()) {
            attemptPos--;
        }
        list.remove(i);
        list.add(attemptPos, item);
    }

}
