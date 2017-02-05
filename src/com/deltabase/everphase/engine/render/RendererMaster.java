package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.engine.model.ModelTextured;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Settings;
import com.deltabase.everphase.terrain.Terrain;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.deltabase.everphase.api.EverPhaseApi.RendererUtils.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;

public class RendererMaster {

    public static final Vector3f SKY_COLOR = new Vector3f(0.446F, 0.696F, 0.946F);   //The color of the sky

    private Map<ModelTextured, List<Entity>> entities = new HashMap<>();    //A Map that links multiple entities that share the same model to that model
    private Map<ModelTextured, List<Entity>> entitiesNM = new HashMap<>();    //A Map that links multiple entities that share the same model to that model

    private List<Terrain> terrains = new ArrayList<>(); //A list of all terrains in the game

    public RendererMaster() {
        enableCulling();                                //Enable culling. This disables the rendering of the not seen triangles
    }

    /**
     * Disables rendering of not seen triangles
     */
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Enables rendering og not seen triangles
     */
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Helper function that renders everything in the game.
     *
     * @param entities  A list of all entities to render
     * @param terrains  A list of all terrains to render
     * @param lights    A list of all lights to render
     * @param player    The player to render the camera in the right place
     * @param clipPlane The plane where everything above stops rendering
     */
    public void renderScene(List<Entity> entities, List<Entity> entitiesNM, List<Terrain> terrains, List<EntityLight> lights, EntityPlayer player, Vector4f clipPlane) {
        for(Terrain terrain : terrains) processTerrains(terrain);
        for(Entity entity : entities) processEntity(entity);
        for (Entity entity : entitiesNM) processEntityNM(entity);

        this.render(player, lights, clipPlane);
    }

    /**
     * Renders the camera, entities, terrains and lights into the world
     *
     * @param player    The player to render
     * @param lights    The lights to light up the entities
     * @param clipPlane The plane where everything above stops rendering
     */
    public void render(EntityPlayer player, List<EntityLight> lights, Vector4f clipPlane) {
        init();
        RENDERER_ENTITY.getShader().start();
        RENDERER_ENTITY.getShader().loadClipPlane(clipPlane);
        RENDERER_ENTITY.getShader().loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        RENDERER_ENTITY.getShader().loadLights(lights);
        RENDERER_ENTITY.getShader().loadViewMatrix(player);

        RENDERER_ENTITY.render(entities);

        RENDERER_TERRAIN.getShader().stop();

        RENDERER_NM.render(entitiesNM, clipPlane, lights, player);

        RENDERER_TERRAIN.getShader().start();
        RENDERER_TERRAIN.getShader().loadClipPlane(clipPlane);
        RENDERER_TERRAIN.getShader().loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        RENDERER_TERRAIN.getShader().loadLights(lights);
        RENDERER_TERRAIN.getShader().loadViewMatrix(player);
        RENDERER_TERRAIN.render(terrains, RENDERER_SHADOW_MAP.getToShadowMapSpaceMatrix());
        RENDERER_TERRAIN.getShader().stop();

        RENDERER_SKYBOX.render(player, SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);


        entities.clear();
        entitiesNM.clear();
        terrains.clear();
    }

    public void renderShadowMap(List<Entity> entities, List<Entity> entitiesNM, EntityLight sun) {
        if(Settings.shadowQuality > 1) {
            for (Entity entity : entities) processEntity(entity);
            for (Entity entity : entitiesNM) processEntityNM(entity);
        }
        RENDERER_SHADOW_MAP.render(this.entities, this.entitiesNM, sun);

    }

    /**
     * Reset everything to start a new frame
     */
    private void init() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);                                          //Enable the depthtest to stop rendering triangles that are behind others
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0F);            //Clears the screen to the sky color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);    //Reset the color buffer- and depth buffer bit
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL_TEXTURE_2D, RENDERER_SHADOW_MAP.getShadowMap());
    }

    /**
     * Add the passed terrain to the list of terrains
     */
    private void processTerrains(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Add the passed entity to the list of entities and link it to
     * other entities with the same model
     */
    private void processEntity(Entity entity) {
        ModelTextured entityModel = entity.getModel();      //Get the model of the entity
        List<Entity> batch = entities.get(entityModel);     //Get the list of entities linked to that model

        if(batch != null)                                   //If there are already some entities in that list...
            batch.add(entity);                              //...add the passed entity to them
        else {                                              //...otherwise...
            List<Entity> newBatch = new ArrayList<>();      //...create a new list...
            newBatch.add(entity);                           //...add the passed entity to this list...
            entities.put(entityModel, newBatch);            //...and link the newly created list to the new model
        }
    }

    /**
    * Add the passed entity to the list of entities and link it to
    * other entities with the same model
    */
    private void processEntityNM(Entity entity) {
        ModelTextured entityModel = entity.getModel();      //Get the model of the entity
        List<Entity> batch = entitiesNM.get(entityModel);     //Get the list of entities linked to that model

        if (batch != null)                                   //If there are already some entities in that list...
            batch.add(entity);                              //...add the passed entity to them
        else {                                              //...otherwise...
            List<Entity> newBatch = new ArrayList<>();      //...create a new list...
            newBatch.add(entity);                           //...add the passed entity to this list...
            entitiesNM.put(entityModel, newBatch);            //...and link the newly created list to the new model
        }
    }

    /**
     * Cleanup method to clear the memory after using it
     */
    public void clean() {
        RENDERER_TERRAIN.getShader().clean();
        RENDERER_ENTITY.getShader().clean();
        RENDERER_WATER.clean();
        RENDERER_GUI.clean();
        RENDERER_NM.clean();
        RENDERER_SHADOW_MAP.clean();
    }
}
