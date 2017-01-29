package com.werwolv.game.entity;

import com.werwolv.game.model.ModelTextured;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.modelloader.NormalMappedObjLoader;
import com.werwolv.game.modelloader.OBJModelLoader;
import com.werwolv.game.resource.TextureModel;
import org.joml.Vector3f;

public class Entity {

    public static final float GRAVITY = -60.0F;        //Gravity constance. Used for jumping in the moment
    public Vector3f position;      //Position of the entity in the world
    private ModelTextured model;    //Model and texture of the entity
    private float rotX, rotY, rotZ; //Rotation of the entity
    private float scale;            //Size of the entity

    private int textureIndex = 0;   //Address of the texture stored in memory
    private ResourceLoader loader;

    public Entity(ResourceLoader loader, String modelPath, String texturePath, Vector3f position, Vector3f rotation, float scale, boolean hasNormalMap) {
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath, loader), new TextureModel(loader.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(loader.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(loader.loadTexture(texturePath)));
        }

        this.loader = loader;
        this.position = position;
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
    }

    public Entity(ResourceLoader loader, String modelPath, String texturePath, int index, Vector3f position, Vector3f rotation, float scale, boolean hasNormalMap) {
        this.textureIndex = index;
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath, loader), new TextureModel(loader.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(loader.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(loader.loadTexture(texturePath)));
        }

        this.loader = loader;
        this.position = position;
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
    }

    public Entity(Vector3f position, float rotation, float scale) {
        this.loader = null;
        this.position = position;
        this.rotX = rotation;
        this.rotY = rotation;
        this.rotZ = rotation;
        this.scale = scale;
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

    public void setModel(ModelTextured model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public ResourceLoader getLoader() {
        return loader;
    }
}
