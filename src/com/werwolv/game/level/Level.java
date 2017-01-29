package com.werwolv.game.level;

import com.werwolv.game.callback.KeyCallback;
import com.werwolv.game.entity.Entity;
import com.werwolv.game.entity.EntityLight;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.gui.Gui;
import com.werwolv.game.main.Main;
import com.werwolv.game.main.Settings;
import com.werwolv.game.render.postProcessing.*;
import com.werwolv.game.terrain.Terrain;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.render.RendererMaster;
import com.werwolv.game.terrain.TileWater;
import com.werwolv.game.toolbox.ScreenShotHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;

public abstract class Level {

    protected ResourceLoader loader = new ResourceLoader();

    protected RendererMaster renderer;

    protected List<Entity> entities    = new ArrayList<>();
    protected List<Entity> entitiesNM  = new ArrayList<>();
    protected List<Terrain> terrains   = new ArrayList<>();
    protected List<EntityLight> lights = new ArrayList<>();
    protected List<TileWater> waters   = new ArrayList<>();
    protected List<Gui> guis           = new ArrayList<>();

    protected EntityPlayer player;

    public Level(EntityPlayer player) {
        this.player = player;
        this.renderer = new RendererMaster(loader, player);
    }

    public void reInitRenderer() {
        this.renderer = new RendererMaster(loader, player);
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
    }

    public ResourceLoader getLoader() {
        return loader;
    }

    public RendererMaster getRenderer() {
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
