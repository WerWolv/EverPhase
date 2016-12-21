package com.werwolv.entity;

import com.werwolv.input.KeyListener;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {

    }

    public void moveCamera() {
        if(KeyListener.keys[GLFW_KEY_W]) position.z -= 0.02F;
        if(KeyListener.keys[GLFW_KEY_S]) position.z += 0.02F;
        if(KeyListener.keys[GLFW_KEY_A]) position.x -= 0.02F;
        if(KeyListener.keys[GLFW_KEY_D]) position.x += 0.02F;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
