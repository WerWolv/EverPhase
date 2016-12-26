package com.werwolv.input;

import com.werwolv.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorListener extends GLFWCursorPosCallback {


    @Override
    public void invoke(long window, double xpos, double ypos) {
        GLFW.glfwSetCursorPos(window, 300, 300);        //Holds the cursor on a fixed position on the screen

        Main.getPlayer().setPitch(Main.getPlayer().getPitch() + (float)(ypos - 300) / 10);    //Transfer the vertical cursor movement to the camera
        Main.getPlayer().setYaw(Main.getPlayer().getYaw() + (float)(xpos - 300) / 10);        //Transfer the horizontal cursor movement to the camera

        if(Main.getPlayer().getPitch() > 90.0F)  Main.getPlayer().setPitch(90.0F);            //Caps the vertical view angle to max. 90°...
        if(Main.getPlayer().getPitch() < -90.0F) Main.getPlayer().setPitch(-90.0F);           //...and to -90°
    }
}
