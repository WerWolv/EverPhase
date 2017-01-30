package com.deltabase.everphase.entity.particle;


import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.resource.TextureParticle;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.toolbox.ParticleHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EntityParticle extends Entity {

    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;

    private float elapsedTime;

    private TextureParticle texture;

    private Vector2f texOffset1 = new Vector2f();
    private Vector2f texOffset2 = new Vector2f();
    private float blend;

    private float distanceToCamera;

    private Vector3f change = new Vector3f();

    public EntityParticle(TextureParticle texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        super(position, rotation, scale);
        this.texture = texture;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;

        ParticleHelper.addParticle(this);
    }

    public boolean update(EntityPlayer player) {
        velocity.y += Entity.GRAVITY * gravityEffect * Main.getFrameTimeSeconds();
        change.set(velocity);

        change.mul(Main.getFrameTimeSeconds());

        distanceToCamera = player.getPosition().sub(position, new Vector3f()).lengthSquared();

        position.add(change);
        updateTextureCoordInfo();

        elapsedTime += Main.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordInfo() {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumOfRows() * texture.getNumOfRows();
        float atlasProgress = lifeFactor * stageCount;

        int index1 = (int) Math.floor(atlasProgress);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgress % 1;
        setTextureOffset(texOffset1, index1);
        setTextureOffset(texOffset2, index2);
    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % texture.getNumOfRows();
        int row = index / texture.getNumOfRows();

        offset.x = (float) column / texture.getNumOfRows();
        offset.y = (float) row / texture.getNumOfRows();
    }

    public Vector3f getPosition() {
        return position;
    }

    public TextureParticle getTexture() {
        return texture;
    }

    public Vector2f getTexOffset1() {
        return texOffset1;
    }

    public Vector2f getTexOffset2() {
        return texOffset2;
    }

    public float getBlend() {
        return blend;
    }

    public float getDistanceToCamera() {
        return distanceToCamera;
    }
}
