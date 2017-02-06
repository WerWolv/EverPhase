package com.deltabase.everphase.callback;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPositionCallback extends GLFWCursorPosCallback {

    public static double xPos, yPos;
    private static boolean enabled = true;

    public static void enableCursorListener(boolean enabled) {
        CursorPositionCallback.enabled = enabled;
        GLFW.glfwSetCursorPos(Main.getWindow(), Main.getWindowSize()[0] / 2, Main.getWindowSize()[1] / 2);        //Holds the cursor on a fixed position on the screen
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        CursorPositionCallback.xPos = xpos;
        CursorPositionCallback.yPos = ypos;

        if (!enabled) return;

        GLFW.glfwSetCursorPos(window, Main.getWindowSize()[0] / 2, Main.getWindowSize()[1] / 2);        //Holds the cursor on a fixed position on the screen

        EverPhaseApi.getEverPhase().thePlayer.setPitch(EverPhaseApi.getEverPhase().thePlayer.getPitch() + (float) (ypos - Main.getWindowSize()[1] / 2) / 10);    //Transfer the vertical cursor movement to the camera
        EverPhaseApi.getEverPhase().thePlayer.setYaw(EverPhaseApi.getEverPhase().thePlayer.getYaw() + (float) (xpos - Main.getWindowSize()[0] / 2) / 10);        //Transfer the horizontal cursor movement to the camera

        if (EverPhaseApi.getEverPhase().thePlayer.getPitch() > 90.0F)
            EverPhaseApi.getEverPhase().thePlayer.setPitch(90.0F);            //Caps the vertical view angle to max. 90°...
        if (EverPhaseApi.getEverPhase().thePlayer.getPitch() < -90.0F)
            EverPhaseApi.getEverPhase().thePlayer.setPitch(-90.0F);           //...and to -90°
    }
}
