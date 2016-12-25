package com.werwolv.entity;

import com.werwolv.input.KeyListener;
import com.werwolv.model.ModelTextured;
import com.werwolv.terrain.Terrain;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;


public class EntityPlayer extends Entity{

    private static final float GRAVITY = -0.03F;
    private static final float PLAYER_HEIGHT = 4.0F;

    private float speedX, speedY, speedZ;
    private boolean isInAir = false;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public EntityPlayer(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(null, position, rotX, rotY, rotZ, scale);
    }


    public void move(Terrain terrain) {
        speedX = speedZ = 0;

        speedY += GRAVITY;

        if(KeyListener.isKeyPressed(GLFW_KEY_W)) { speedX = 0.4F * (float)Math.sin(Math.toRadians(getYaw())); speedZ = -0.4F * (float)Math.cos(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)) { speedX = -0.4F * (float)Math.sin(Math.toRadians(getYaw())); speedZ = 0.4F * (float)Math.cos(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_A)) { speedX = -0.4F * (float)Math.cos(Math.toRadians(getYaw())); speedZ = -0.4F * (float)Math.sin(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)) { speedX = 0.4F * (float)Math.cos(Math.toRadians(getYaw())); speedZ = 0.4F * (float)Math.sin(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !isInAir) {
            speedY = 0.5F;
            isInAir = true;
        }

        addPosition(speedX, speedY, speedZ);

        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z) + PLAYER_HEIGHT;
        if(position.y < terrainHeight) {
            speedY = 0;
            isInAir = false;
            position.y = terrainHeight;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public void addPosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
