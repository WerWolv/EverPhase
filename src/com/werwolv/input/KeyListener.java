package com.werwolv.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyListener extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[1024];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }


    public static boolean isKeyPressed(int key) {
        if(key < 0 || key >= 1024) return false;
        return keys[key];
    }
}
