package com.deltabase.everphase.entity;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.model.ModelTextured;
import com.deltabase.everphase.engine.modelloader.NormalMappedObjLoader;
import com.deltabase.everphase.engine.modelloader.OBJModelLoader;
import com.deltabase.everphase.engine.resource.TextureModel;
import org.joml.Vector3f;

public class Entity {

    protected static final float GRAVITY = -60.0F;        //Gravity constance. Used for jumping in the moment
    protected Vector3f position;      //Position of the entity in the world
    protected float rotX, rotY, rotZ; //Rotation of the entity
    private ModelTextured model;    //Model and texture of the entity
    private float scale;            //Size of the entity
    private boolean hasNormalMap;

    private int textureIndex = 0;   //Address of the texture stored in memory

    public Entity(String modelPath, String texturePath, Vector3f rotation, float scale, boolean hasNormalMap) {
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(EverPhaseApi.RESOURCE_LOADER.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
        }

        this.position = new Vector3f(0, 0, 0);
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
        this.hasNormalMap = hasNormalMap;
    }

    public Entity(String modelPath, String texturePath, int index, Vector3f rotation, float scale, boolean hasNormalMap) {
        this.textureIndex = index;
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(EverPhaseApi.RESOURCE_LOADER.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
        }
        this.position = new Vector3f(0, 0, 0);
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
        this.hasNormalMap = hasNormalMap;
    }

    public Entity(float rotation, float scale) {
        this.position = new Vector3f(0, 0, 0);
        this.rotX = rotation;
        this.rotY = rotation;
        this.rotZ = rotation;
        this.scale = scale;
        this.hasNormalMap = false;
    }

    /*
     * Calculates the x coordinate of the texture inside the texture
     * atlas.
     *
     * @return  The X-Coordinate of the texture in the texture atlas
     */
    public float getTextureXOffset() {
        int col = textureIndex % model.getTexture().getNumOfRows();
        return (float) col / (float) model.getTexture().getNumOfRows();
    }

    /*
     * Calculates the y coordinate of the texture inside the texture
     * atlas.
     *
     * @return  The y-Coordinate of the texture in the texture atlas
     */
    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumOfRows();
        return (float) row / (float) model.getTexture().getNumOfRows();
    }

    /*
     * Increases the position of the entity.
     *
     * @param dx    The amount to increase the position of the entity in the x direction.
     * @param dy    The amount to increase the position of the entity in the y direction.
     * @param dz    The amount to increase the position of the entity in the z direction.
     */
    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /*
     * Increases the position of the entity.
     *
     * @param dx    The amount to increase the rotation of the entity in the x direction.
     * @param dy    The amount to increase the rotation of the entity in the y direction.
     * @param dz    The amount to increase the rotation of the entity in the z direction.
     */
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /* Getters and Setters */

    public ModelTextured getModel() {
        return model;
    }

    public Entity setModel(ModelTextured model) {
        this.model = model;

        return this;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Entity setPosition(Vector3f position) {
        this.position = position;

        return this;
    }

    public float getRotX() {
        return rotX;
    }

    public Entity setRotX(float rotX) {
        this.rotX = rotX;

        return this;
    }

    public float getRotY() {
        return rotY;
    }

    public Entity setRotY(float rotY) {
        this.rotY = rotY;

        return this;
    }

    public float getRotZ() {
        return rotZ;
    }

    public Entity setRotZ(float rotZ) {
        this.rotZ = rotZ;

        return this;
    }

    public float getScale() {
        return scale;
    }

    public Entity setScale(float scale) {
        this.scale = scale;

        return this;
    }

    public boolean isHasNormalMap() {
        return hasNormalMap;
    }
}
