package com.werwolv.game.render;

import com.werwolv.game.entity.Entity;
import com.werwolv.game.entity.EntityLight;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.shader.ShaderEntity;
import com.werwolv.game.shader.ShaderTerrain;
import com.werwolv.game.terrain.Terrain;
import com.werwolv.game.main.Main;
import com.werwolv.game.model.ModelTextured;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.render.shadow.RendererShadowMapMaster;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;

public class RendererMaster {

    public static final Vector3f SKY_COLOR = new Vector3f(0.5F, 0.5F, 0.5F);   //The color of the sky
    public static final float FOV = 70;                //The field of view
    public static final float NEAR_PLANE = 0.01F;       //The plane to start rendering
    public static final float FAR_PLANE = 1000.0F;     //The plane to stop rendering
    private Matrix4f projectionMatrix;                  //The projection matrix

    private ShaderEntity shaderEntity = new ShaderEntity();     //The shader to use for rendering entities
    private ShaderTerrain shaderTerrain = new ShaderTerrain();  //The shader to use for rendering the terrain

    private RendererEntity  rendererEntity;             //The renderer to render entities
    private RendererNormalMapping rendererNM;
    private RendererTerrain rendererTerrain;            //The renderer to render the terrain
    private RendererSkybox  rendererSkybox;             //The renderer to render the skybox
    private RendererWater   rendererWater;              //The renderer to render all water planes
    private RendererGui     rendererGui;
    private RendererShadowMapMaster shadowMapMasterRenderer;

    /* Shadow Renderer */

    private ResourceLoader loader;

    private Map<ModelTextured, List<Entity>> entities = new HashMap<>();    //A Map that links multiple entities that share the same model to that model
    private Map<ModelTextured, List<Entity>> entitiesNM = new HashMap<>();    //A Map that links multiple entities that share the same model to that model

    private List<Terrain> terrains = new ArrayList<>(); //A list of all terrains in the game

    public RendererMaster(ResourceLoader loader, EntityPlayer player) {
        enableCulling();                                //Enable culling. This disables the rendering of the not seen triangles

        createProjectionMatrix();                       //Create a new projection matrix

        this.loader = loader;

        //Renderer initializing

        rendererEntity = new RendererEntity(shaderEntity, projectionMatrix);
        rendererNM = new RendererNormalMapping(projectionMatrix);
        rendererTerrain = new RendererTerrain(shaderTerrain, projectionMatrix);
        rendererSkybox = new RendererSkybox(loader, projectionMatrix);
        rendererWater = new RendererWater(loader, projectionMatrix, NEAR_PLANE, FAR_PLANE);
        rendererGui = new RendererGui(loader);

        shadowMapMasterRenderer = new RendererShadowMapMaster(player);
    }

    /*
     * Disables rendering of not seen triangles
     */
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /*
     * Enables rendering og not seen triangles
     */
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /*
     * Helper function that renders everything in the game.
     *
     * @param entities  A list of all entities to render
     * @param terrains  A list of all terrains to render
     * @param waters    A list of every water plane to render
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

    /*
     * Renders the camera, entities, terrains and lights into the world
     *
     * @param player    The player to render
     * @param lights    The lights to light up the entities
     */
    public void render(EntityPlayer player, List<EntityLight> lights, Vector4f clipPlane) {
        init();
        shaderEntity.start();
        shaderEntity.loadClipPlane(clipPlane);
        shaderEntity.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        shaderEntity.loadLights(lights);
        shaderEntity.loadViewMatrix(player);

        rendererEntity.render(entities);

        shaderEntity.stop();

        rendererNM.render(entitiesNM, clipPlane, lights, player);

        shaderTerrain.start();
        shaderTerrain.loadClipPlane(clipPlane);
        shaderTerrain.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        shaderTerrain.loadLights(lights);
        shaderTerrain.loadViewMatrix(player);
        rendererTerrain.render(terrains, shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
        shaderTerrain.stop();

        rendererSkybox.render(player, SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);


        entities.clear();
        entitiesNM.clear();
        terrains.clear();
    }

    public void renderShadowMap(List<Entity> entities, List<Entity> entitiesNM, EntityLight sun) {
        for(Entity entity : entities) processEntity(entity);
        for (Entity entity : entitiesNM) processEntityNM(entity);

        shadowMapMasterRenderer.render(this.entities, this.entitiesNM, sun);
    }

    /*
     * Reset everything to start a new frame
     */
    private void init() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);                                          //Enable the depthtest to stop rendering triangles that are behind others
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0F);            //Clears the screen to the sky color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);    //Reset the color buffer- and depth buffer bit
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL_TEXTURE_2D, getShadowMapTexture());
    }

    /*
     * Create a new projection matrix
     */
    private void createProjectionMatrix(){
        projectionMatrix = new Matrix4f();
        float aspectRatio = Main.getAspectRatio();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
    /*
     * Add the passed terrain to the list of terrains
     */
    private void processTerrains(Terrain terrain) {
        terrains.add(terrain);
    }

    /*
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

    /*
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

    /*
     * Cleanup method to clear the memory after using it
     */
    public void clean() {
        shaderEntity.clean();
        shaderTerrain.clean();
        rendererWater.clean();
        rendererGui.clean();
        rendererNM.clean();
        shadowMapMasterRenderer.cleanUp();
    }

    /* Getter */

    public Matrix4f getProjectionMatrix() { return projectionMatrix; }

    public RendererEntity getRendererEntity() {
        return rendererEntity;
    }

    public RendererTerrain getRendererTerrain() {
        return rendererTerrain;
    }

    public RendererSkybox getRendererSkybox() {
        return rendererSkybox;
    }

    public RendererWater getRendererWater() {
        return rendererWater;
    }

    public RendererGui getRendererGui() {
        return rendererGui;
    }

    public ResourceLoader getModelLoader() {
        return loader;
    }

    public int getShadowMapTexture() {
        return shadowMapMasterRenderer.getShadowMap();
    }
}
