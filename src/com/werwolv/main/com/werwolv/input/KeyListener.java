package com.werwolv.main.com.werwolv.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyListener extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[1024];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

}
