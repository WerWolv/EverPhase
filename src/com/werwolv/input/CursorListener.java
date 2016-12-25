package com.werwolv.input;

import com.werwolv.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorListener extends GLFWCursorPosCallback {

    //private static final PITCH_MAX =


    @Override
    public void invoke(long window, double xpos, double ypos) {
        GLFW.glfwSetCursorPos(window, 300, 300);

        Main.camera.setPitch(Main.camera.getPitch() + (float)(ypos - 300) / 10);    //Vertical Camera move
        Main.camera.setYaw(Main.camera.getYaw() + (float)(xpos - 300) / 10);        //Horizontal Camera move

        if(Main.camera.getPitch() > 90.0F)  Main.camera.setPitch(90.0F);
        if(Main.camera.getPitch() < -90.0F) Main.camera.setPitch(-90.0F);
    }
}
