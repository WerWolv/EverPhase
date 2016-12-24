package com.werwolv.input;

import com.werwolv.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorListener extends GLFWCursorPosCallback {


    @Override
    public void invoke(long window, double xpos, double ypos) {
        GLFW.glfwSetCursorPos(window, 300, 300);

        Main.camera.setPitch(Main.camera.getPitch() + (float)(ypos - 300) / 10);    //Vertical Camera move
        Main.camera.setYaw(Main.camera.getYaw() + (float)(xpos - 300) / 10);        //Horizontal Camera move

    }
}
