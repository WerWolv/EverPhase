package com.werwolv.entity.particle;

import com.werwolv.entity.Entity;
import com.werwolv.main.Main;
import org.joml.Vector3f;

public class EntityParticle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;

    public EntityParticle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;

        ParticleManager.addParticle(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    protected boolean update() {
        velocity.y += Entity.GRAVITY * gravityEffect * Main.getFrameTimeSeconds();
        Vector3f velocityChange = new Vector3f(velocity);
        velocityChange.mul(Main.getFrameTimeSeconds());
        position.add(velocityChange);
        elapsedTime += Main.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }
}
