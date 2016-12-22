package com.werwolv.entity;

import com.werwolv.input.KeyListener;
import org.joml.Vector3f;


public class EntityCamera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public EntityCamera() {

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
