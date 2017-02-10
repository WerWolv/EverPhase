package com.deltabase.everphase.entity;

import com.deltabase.everphase.api.Capability;
import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.IUpdateable;
import com.deltabase.everphase.api.event.entity.EntityDeathEvent;
import com.deltabase.everphase.collision.AABB;
import com.deltabase.everphase.damageSource.DamageSource;
import com.deltabase.everphase.damageSource.DamageSourceMagic;
import com.deltabase.everphase.engine.model.ModelTextured;
import com.deltabase.everphase.engine.modelloader.NormalMappedObjLoader;
import com.deltabase.everphase.engine.modelloader.OBJModelLoader;
import com.deltabase.everphase.engine.resource.TextureModel;
import org.joml.Vector3f;

public class Entity implements IUpdateable {

    protected static final float GRAVITY = -60.0F;        //Gravity constance. Used for jumping in the moment
    protected static final float PLAYER_HEIGHT = 0.0F;    //Height of the player to render the camera above the ground.

    public final Capability HEALTH = new Capability(100, 100);
    public final Capability ENERGY = new Capability(100, 100);
    public final Capability SPEED = new Capability(20, 20);

    protected Vector3f position;      //Position of the entity in the world
    protected float rotX, rotY, rotZ; //Rotation of the entity
    protected ModelTextured model;    //Model and texture of the entity
    protected float scale;            //Size of the entity
    protected boolean hasNormalMap;
    protected boolean isAlive = true;
    protected DamageSource damageSourceOnDeath = new DamageSourceMagic();
    protected int textureIndex = 0;   //Address of the texture stored in memory

    protected AABB boundingBox;
    protected Vector3f bbSize;

    public Entity(String modelPath, String texturePath, Vector3f rotation, float scale, Vector3f bbSize, boolean hasNormalMap) {
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(EverPhaseApi.RESOURCE_LOADER.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
        }

        this.position = new Vector3f(0, 0, 0);
        this.boundingBox = new AABB(position, bbSize.mul(scale));
        this.bbSize = bbSize.mul(scale);
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
        this.hasNormalMap = hasNormalMap;

        setUpdatable();
    }

    public Entity(String modelPath, String texturePath, int index, Vector3f rotation, float scale, Vector3f bbSize, boolean hasNormalMap) {
        this.textureIndex = index;
        if (!(modelPath.equals("") || texturePath.equals(""))) {
            if (hasNormalMap)
                this.model = new ModelTextured(NormalMappedObjLoader.loadOBJ(modelPath), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
            else
                this.model = new ModelTextured(EverPhaseApi.RESOURCE_LOADER.loadToVAO(OBJModelLoader.loadOBJ(modelPath)), new TextureModel(EverPhaseApi.RESOURCE_LOADER.loadTexture(texturePath)));
        }
        this.position = new Vector3f(0, 0, 0);
        this.boundingBox = new AABB(position, bbSize.mul(scale));
        this.bbSize = bbSize.mul(scale);
        this.rotX = rotation.x;
        this.rotY = rotation.y;
        this.rotZ = rotation.z;
        this.scale = scale;
        this.hasNormalMap = hasNormalMap;

        setUpdatable();
    }

    public Entity(float rotation, float scale, Vector3f bbSize) {
        this.position = new Vector3f(0, 0, 0);
        this.boundingBox = new AABB(position, bbSize.mul(scale));
        this.bbSize = bbSize.mul(scale);
        this.rotX = rotation;
        this.rotY = rotation;
        this.rotZ = rotation;
        this.scale = scale;
        this.hasNormalMap = false;

        setUpdatable();
    }


    @Override
    public void update() {
        if (HEALTH.getValue() <= 0) this.setDead();

        this.boundingBox = new AABB(new Vector3f(position).sub(0.0F, PLAYER_HEIGHT, 0.0F), bbSize);

    }

    /**
     * Calculates the x coordinate of the texture inside the texture
     * atlas.
     *
     * @return  The X-Coordinate of the texture in the texture atlas
     */
    public float getTextureXOffset() {
        int col = textureIndex % model.getTexture().getNumOfRows();
        return (float) col / (float) model.getTexture().getNumOfRows();
    }

    /**
     * Calculates the y coordinate of the texture inside the texture
     * atlas.
     *
     * @return  The y-Coordinate of the texture in the texture atlas
     */
    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumOfRows();
        return (float) row / (float) model.getTexture().getNumOfRows();
    }

    /**
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

    /**
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setDead() {
        isAlive = false;
    }

    public void damageEntity(float damage, DamageSource damageSource) {
        float newHealth = this.HEALTH.getValue() - damage;

        if (newHealth < 0) {
            this.damageSourceOnDeath = damageSource;
            EverPhaseApi.EVENT_BUS.postEvent(new EntityDeathEvent(this, damageSource));
        }

        this.HEALTH.setValue(this.HEALTH.getValue() - damage);
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }


}
