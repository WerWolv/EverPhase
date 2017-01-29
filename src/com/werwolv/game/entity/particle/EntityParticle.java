package com.werwolv.game.entity.particle;


import com.werwolv.game.entity.Entity;
import com.werwolv.game.main.Main;
import com.werwolv.game.toolbox.ParticleHelper;
import org.joml.Vector3f;

public class EntityParticle extends Entity {

    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;

    private float elapsedTime;

    public EntityParticle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        super(position, rotation, scale);
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;

        ParticleHelper.addParticle(this);
    }

    public boolean update() {
        velocity.y += Entity.GRAVITY * gravityEffect * Main.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);

        change = change.mul(Main.getFrameTimeSeconds(), new Vector3f());

        position = change.add(position, new Vector3f());
        elapsedTime += Main.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }

    public Vector3f getPosition() {
        return position;
    }
}
