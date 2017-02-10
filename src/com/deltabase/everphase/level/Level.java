package com.deltabase.everphase.level;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.IUpdateable;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.engine.audio.AudioHelper;
import com.deltabase.everphase.engine.fbo.FrameBufferObject;
import com.deltabase.everphase.engine.render.postProcessing.*;
import com.deltabase.everphase.engine.toolbox.ParticleHelper;
import com.deltabase.everphase.engine.toolbox.ScreenShotHelper;
import com.deltabase.everphase.engine.toolbox.TextRenderingHelper;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.main.Settings;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.terrain.TileWater;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Level {

    protected EntityLight entitySun = new EntityLight(new Vector3f(1, 0.9F, 0.9F), new Vector3f(1, 0, 0));
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> entitiesNM = new ArrayList<>();
    private List<Terrain> terrains = new ArrayList<>();
    private List<EntityLight> lights = new ArrayList<>();
    private List<TileWater> waters = new ArrayList<>();
    private FrameBufferObject postProcessing = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1]);
    private FrameBufferObject outputFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);

    public Level() {
        ParticleHelper.init();
    }

    public abstract void initLevel();

    public void updateLevel() {
        glfwPollEvents();
        ParticleHelper.update(EverPhaseApi.getEverPhase().thePlayer);

        EverPhaseApi.getUpdateables().forEach(IUpdateable::update);

        removeDeadEntities();
    }

    public void renderLevel() {
        EverPhaseApi.RendererUtils.RENDERER_MASTER.renderShadowMap(this.getEntities(), this.getEntitiesNM(), entitySun);

        for (TileWater water : this.getWaters())
            water.renderWaterEffects();


        postProcessing.bindFrameBuffer();
        EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(this.getEntities(), this.getEntitiesNM(), this.getTerrains(), this.getLights(), EverPhaseApi.getEverPhase().thePlayer, new Vector4f(0, -1, 0, 100000));
        EverPhaseApi.RendererUtils.RENDERER_WATER.render(this.getWaters(), this.getLights(), EverPhaseApi.getEverPhase().thePlayer);

        ParticleHelper.renderParticles(EverPhaseApi.getEverPhase().thePlayer);

        postProcessing.unbindFrameBuffer();

        postProcessing.resolveToFBO(outputFBO);
        PostProcessing.doPostProcessing(outputFBO.getColorTexture());
    }

    public void renderGUI() {
        for (Gui gui : EverPhaseApi.GuiUtils.getRegisteredHuds())
            EverPhaseApi.RendererUtils.RENDERER_GUI.render(gui);

        if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() != null) {
            EverPhaseApi.RendererUtils.RENDERER_GUI.render(EverPhaseApi.getEverPhase().thePlayer.getCurrentGui());
        }

        TextRenderingHelper.renderTexts();
    }

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
        if (KeyCallback.isKeyPressedEdge(GLFW_KEY_F3))
            Settings.showBoundingBoxes = !Settings.showBoundingBoxes;


        EverPhaseApi.getEverPhase().thePlayer.onMove(getCurrTerrain());
        EverPhaseApi.getEverPhase().thePlayer.onInteract();

    }

    public void clean() {
        postProcessing.clean();
        outputFBO.clean();

        entities.clear();
        terrains.clear();
        lights.clear();
        waters.clear();

        ParticleHelper.clean();
        PostProcessing.clean();
        TextRenderingHelper.clean();
        AudioHelper.clean();
    }

    public void spawnEntity(Entity entity, Vector3f position) {
        if (entity instanceof EntityLight)
            lights.add((EntityLight) entity.setPosition(position));
        else if (entity.isHasNormalMap())
            entitiesNM.add(entity.setPosition(position));
        else entities.add(entity.setPosition(position));
    }

    public void spawnEntities(List<Entity> entities) {
        entities.forEach(e -> {
            if (e instanceof EntityLight)
                this.lights.add((EntityLight) e);
            else if (e.isHasNormalMap())
                this.entitiesNM.add(e);
            else this.entities.add(e);
        });
    }

    public void addTerrain(Terrain terrain, int gridX, int gridY) {
        terrain.setGridPosition(gridX, gridY);
        terrains.add(terrain);
    }

    public void addWaterPlane(TileWater water, Vector3f position) {
        water.setCenterPosition(position.x, position.z, position.y);
        waters.add(water);
    }

    public void removeDeadEntities() {
        Iterator<Entity> entityIterator = getEntities().iterator();
        Iterator<Entity> entityNMIterator = getEntitiesNM().iterator();

        while (entityIterator.hasNext())
            if (!entityIterator.next().isAlive())
                entityIterator.remove();

        while (entityNMIterator.hasNext())
            if (!entityNMIterator.next().isAlive())
                entityNMIterator.remove();
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

    public Terrain getCurrTerrain() {
        for(Terrain terrain : terrains)
            if (terrain.getX() >= EverPhaseApi.getEverPhase().thePlayer.getPosition().x() && terrain.getX() * Terrain.SIZE <= EverPhaseApi.getEverPhase().thePlayer.getPosition().x())
                if (terrain.getZ() >= EverPhaseApi.getEverPhase().thePlayer.getPosition().z() && terrain.getZ() * Terrain.SIZE <= EverPhaseApi.getEverPhase().thePlayer.getPosition().z())
                    return terrain;
        return terrains.get(0);
    }
}
