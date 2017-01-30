package com.deltabase.everphase.level;

import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.main.Settings;
import com.deltabase.everphase.modelloader.ResourceLoader;
import com.deltabase.everphase.render.RendererMaster;
import com.deltabase.everphase.render.postProcessing.*;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.terrain.TileWater;
import com.deltabase.everphase.toolbox.ParticleHelper;
import com.deltabase.everphase.toolbox.ScreenShotHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;

public abstract class Level {

    protected ResourceLoader loader = new ResourceLoader();

    protected static RendererMaster renderer;

    protected List<Entity> entities    = new ArrayList<>();
    protected List<Entity> entitiesNM  = new ArrayList<>();
    protected List<Terrain> terrains   = new ArrayList<>();
    protected List<EntityLight> lights = new ArrayList<>();
    protected List<TileWater> waters   = new ArrayList<>();
    protected List<Gui> guis           = new ArrayList<>();

    protected EntityPlayer player;

    public Level(EntityPlayer player) {
        this.player = player;
        Level.renderer = new RendererMaster(loader, player);
        ParticleHelper.init(loader, renderer.getProjectionMatrix());
    }

    public void reInitRenderer() {
        Level.renderer = new RendererMaster(loader, player);
    }

    public abstract void initLevel();

    public abstract void updateLevel();

    public abstract void renderLevel();

    public abstract void renderGUI();

    public void applyPostProcessingEffects() {
        PostProcessing.init(loader);

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
        guis.clear();
        ParticleHelper.clean();
    }

    public ResourceLoader getLoader() {
        return loader;
    }

    public static RendererMaster getRenderer() {
        return renderer;
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

    public List<Gui> getGuis() {
        return guis;
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
