package com.werwolv.entity.particle;

import com.werwolv.entity.Entity;
import com.werwolv.main.Main;
import com.werwolv.render.RendererParticle;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityParticle {

    private static List<EntityParticle> particles = new ArrayList<>();
    private static RendererParticle rendererParticle;

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
        velocity.y += Entity.GRAVITY * Main.getFrameTimeSeconds();
        Vector3f velocityChange = new Vector3f(velocity);
        velocityChange.mul((float)Main.getFrameTimeSeconds());
        velocityChange.add(position);
        elapsedTime += Main.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }
}
