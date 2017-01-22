package com.werwolv.level;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.gui.Gui;
import com.werwolv.modelloader.ResourceLoader;
import com.werwolv.render.RendererMaster;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    protected ResourceLoader loader = new ResourceLoader();

    protected RendererMaster renderer;

    protected List<Entity> entities = new ArrayList<>();
    protected List<Entity> entitiesNM = new ArrayList<>();
    protected List<Terrain> terrains = new ArrayList<>();
    protected List<EntityLight> lights = new ArrayList<>();
    protected List<TileWater> waters = new ArrayList<>();
    protected List<Gui> guis = new ArrayList<>();

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
