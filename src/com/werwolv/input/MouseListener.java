package com.werwolv.input;


import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;

public class MouseListener extends GLFWMouseButtonCallback{

    private static boolean[] buttons = new boolean[64];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;
    }

    public static boolean isButtonPressed(int button) {
        if(button < 0 || button >= 64) return false;
        return buttons[button];
    }
}
