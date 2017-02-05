package com.deltabase.everphase.level;

import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.engine.render.postProcessing.*;
import com.deltabase.everphase.engine.toolbox.ParticleHelper;
import com.deltabase.everphase.engine.toolbox.ScreenShotHelper;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.main.Settings;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.terrain.TileWater;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;

public abstract class Level {
    protected EntityPlayer player;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> entitiesNM = new ArrayList<>();
    private List<Terrain> terrains = new ArrayList<>();
    private List<EntityLight> lights = new ArrayList<>();
    private List<TileWater> waters = new ArrayList<>();

    public Level(EntityPlayer player) {
        this.player = player;
        ParticleHelper.init();
    }

    public abstract void initLevel();

    public abstract void updateLevel();

    public abstract void renderLevel();

    public abstract void renderGUI();

    public void applyPostProcessingEffects() {
        PostProcessing.init();

        if(Settings.bloom) {
            PostProcessing.applyEffect(new FilterBright(0.6F));
            PostProcessing.applyEffect(new FilterGaussianBlurHorizontal());
            PostProcessing.applyEffect(new FilterGaussianBlurVertical());
            PostProcessing.applyEffect(new FilterBloom());
        }
        PostProcessing.applyEffect(new FilterLightScattering());

        PostProcessing.applyEffect(new FilterVignette());
        PostProcessing.applyEffect(new FilterContrast(0.25F));
    }

    public void handleInput() {
        if (KeyCallback.isKeyPressed(GLFW_KEY_ESCAPE))
            GLFW.glfwSetWindowShouldClose(Main.getWindow(), true);
        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F2))
            ScreenShotHelper.takeScreenShot();
    }

    public void clean() {
        entities.clear();
        terrains.clear();
        lights.clear();
        waters.clear();
        ParticleHelper.clean();
    }

    public void spawnEntity(Entity entity, Vector3f position) {
        if (entity instanceof EntityLight)
            lights.add((EntityLight) entity.setPosition(position));
        else if (entity.isHasNormalMap())
            entitiesNM.add(entity.setPosition(position));
        else entities.add(entity.setPosition(position));
    }

    public void addTerrain(Terrain terrain, int gridX, int gridY) {
        terrain.setGridPosition(gridX, gridY);
        terrains.add(terrain);
    }

    public void addWaterPlane(TileWater water, Vector3f position) {
        water.setCenterPosition(position.x, position.z, position.y);
        waters.add(water);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesNM() {
        return entitiesNM;
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public List<EntityLight> getLights() {
        return lights;
    }

    public List<TileWater> getWaters() {
        return waters;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Terrain getCurrTerrain() {
        for(Terrain terrain : terrains)
            if(terrain.getX() >= player.getPosition().x() && terrain.getX() * Terrain.SIZE <= player.getPosition().x())
                if(terrain.getZ() >= player.getPosition().z() && terrain.getZ() * Terrain.SIZE <= player.getPosition().z())
                    return terrain;
        return terrains.get(0);
    }
}
