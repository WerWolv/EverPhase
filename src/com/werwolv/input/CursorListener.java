package com.werwolv.input;

import com.werwolv.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorListener extends GLFWCursorPosCallback {

    //private static final PITCH_MAX =


    @Override
    public void invoke(long window, double xpos, double ypos) {
        GLFW.glfwSetCursorPos(window, 300, 300);

        Main.getCamera().setPitch(Main.getCamera().getPitch() + (float)(ypos - 300) / 10);    //Vertical Camera move
        Main.getCamera().setYaw(Main.getCamera().getYaw() + (float)(xpos - 300) / 10);        //Horizontal Camera move

        if(Main.getCamera().getPitch() > 90.0F)  Main.getCamera().setPitch(90.0F);
        if(Main.getCamera().getPitch() < -90.0F) Main.getCamera().setPitch(-90.0F);
    }
}
