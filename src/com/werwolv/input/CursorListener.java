package com.werwolv.input;

import com.werwolv.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorListener extends GLFWCursorPosCallback {

    //private static final PITCH_MAX =


    @Override
    public void invoke(long window, double xpos, double ypos) {
        GLFW.glfwSetCursorPos(window, 300, 300);

        Main.getPlayer().setPitch(Main.getPlayer().getPitch() + (float)(ypos - 300) / 10);    //Vertical Camera move
        Main.getPlayer().setYaw(Main.getPlayer().getYaw() + (float)(xpos - 300) / 10);        //Horizontal Camera move

        if(Main.getPlayer().getPitch() > 90.0F)  Main.getPlayer().setPitch(90.0F);
        if(Main.getPlayer().getPitch() < -90.0F) Main.getPlayer().setPitch(-90.0F);
    }
}
