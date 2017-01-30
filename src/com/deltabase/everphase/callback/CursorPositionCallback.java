package com.deltabase.everphase.callback;

import com.deltabase.everphase.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPositionCallback extends GLFWCursorPosCallback {

    private static boolean enabled = true;

    public static void enableCursorListener(boolean enabled) {
        CursorPositionCallback.enabled = enabled;
        GLFW.glfwSetCursorPos(Main.getWindow(), Main.getWindowSize()[0] / 2, Main.getWindowSize()[1] / 2);        //Holds the cursor on a fixed position on the screen
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        if (KeyCallback.isKeyPressed(GLFW.GLFW_KEY_LEFT_ALT) || !enabled) return;

        GLFW.glfwSetCursorPos(window, Main.getWindowSize()[0] / 2, Main.getWindowSize()[1] / 2);        //Holds the cursor on a fixed position on the screen

        Main.getPlayer().setPitch(Main.getPlayer().getPitch() + (float) (ypos - Main.getWindowSize()[1] / 2) / 10);    //Transfer the vertical cursor movement to the camera
        Main.getPlayer().setYaw(Main.getPlayer().getYaw() + (float) (xpos - Main.getWindowSize()[0] / 2) / 10);        //Transfer the horizontal cursor movement to the camera

        if(Main.getPlayer().getPitch() > 90.0F)  Main.getPlayer().setPitch(90.0F);            //Caps the vertical view angle to max. 90°...
        if(Main.getPlayer().getPitch() < -90.0F) Main.getPlayer().setPitch(-90.0F);           //...and to -90°
    }
}
