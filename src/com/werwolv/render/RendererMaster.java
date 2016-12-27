package com.werwolv.render;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.EntityLight;
import com.werwolv.fbo.FrameBufferWater;
import com.werwolv.main.Main;
import com.werwolv.model.ModelTextured;
import com.werwolv.shader.ShaderEntity;
import com.werwolv.shader.ShaderTerrain;
import com.werwolv.shader.ShaderWater;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glClearColor;

public class RendererMaster {

    private static final float FOV = 70;                //The field of view
    private static final float NEAR_PLANE = 0.1F;       //The plane to start rendering
    private static final float FAR_PLANE = 1000.0F;     //The plane to stop rendering

    private static final Vector3f SKY_COLOR = new Vector3f(0.5F, 0.5F, 0.5F);   //The color of the sky

    private Matrix4f projectionMatrix;                  //The projection matrix

    private ShaderEntity shaderEntity = new ShaderEntity();     //The shader to use for rendering entities
    private ShaderTerrain shaderTerrain = new ShaderTerrain();  //The shader to use for rendering the terrain

    private FrameBufferWater fboWater = new FrameBufferWater();

    private RendererEntity  rendererEntity;             //The renderer to render entities
    private RendererTerrain rendererTerrain;            //The renderer to render the terrain
    private RendererSkybox  rendererSkybox;             //The renderer to render the skybox
    private RendererWater   rendererWater;              //The renderer to render all water planes

    private Map<ModelTextured, List<Entity>> entities = new HashMap<>();    //A Map that links multiple entities that share the same model to that model
    private List<Terrain> terrains = new ArrayList<>(); //A list of all terrains in the game

    public RendererMaster(ModelLoader loader) {
        enableCulling();                                //Enable culling. This disables the rendering of the not seen triangles

        createProjectionMatrix();                       //Create a new projection matrix

        //Renderer initializing

        rendererEntity = new RendererEntity(shaderEntity, projectionMatrix);
        rendererTerrain = new RendererTerrain(shaderTerrain, projectionMatrix);
        rendererSkybox = new RendererSkybox(loader, projectionMatrix);
        rendererWater = new RendererWater(loader, projectionMatrix, fboWater);
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
    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<EntityLight> lights, EntityPlayer player, Vector4f clipPlane) {
        for(Terrain terrain : terrains) processTerrains(terrain);
        for(Entity entity : entities) processEntity(entity);

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

        shaderTerrain.start();
        shaderTerrain.loadClipPlane(clipPlane);
        shaderTerrain.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        shaderTerrain.loadLights(lights);
        shaderTerrain.loadViewMatrix(player);
        rendererTerrain.render(terrains);
        shaderTerrain.stop();

        rendererSkybox.render(player, SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        entities.clear();
        terrains.clear();
    }

    /*
     * Reset everything to start a new frame
     */
    private void init() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);                                          //Enable the depthtest to stop rendering triangles that are behind others
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);    //Reset the color buffer- and depth buffer bit
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0F);            //Clears the screen to the sky color

    }

    /*
     * Create a new projection matrix
     */
    private void createProjectionMatrix() {
        float aspectRatio = (float) Main.getWindowSize()[0] / (float) Main.getWindowSize()[1];  //The ratio between the width and the height of the screen
        float yScale = (float) ((1.0F / Math.tan(Math.toRadians(FOV / 2.0F))) * aspectRatio);   //The horizontal camera lens opening angle
        float xScale = yScale / aspectRatio;                                                    //The vertical camera lens opening angle
        float frustumLength = FAR_PLANE - NEAR_PLANE;                                           //The distance between the minimal draw distance and the maximum draw distance

        //Load the parameters to the matrix
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }

    /*
     * Add the passed terrain to the list of terrains
     */
    public void processTerrains(Terrain terrain) {
        terrains.add(terrain);
    }

    /*
     * Add the passed entity to the list of entities and link it to
     * other entities with the same model
     */
    public void processEntity(Entity entity) {
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
     * Cleanup method to clear the memory after using it
     */
    public void clean() {
        shaderEntity.clean();
        shaderTerrain.clean();
        rendererWater.clean();
    }

    /* Getter */

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

    public FrameBufferWater getFboWater() {
        return fboWater;
    }
}
