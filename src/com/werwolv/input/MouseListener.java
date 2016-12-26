package com.werwolv.input;


import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;

public class MouseListener extends GLFWMouseButtonCallback{

    private static boolean[] buttons = new boolean[64];     //Array to store each and every button on the mouse

    /*
     * Called when any button on the mouse got pressed
     *
     * @param window    The id of the current window
     * @param key       The currently pressed button
     * @param scancode  The scancode of the pressed button
     * @param action    If the key got pressed, released or repeated
     * @param mods      Which modifier key got held down (CTRL, SHIFT, ALT)
     */
    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;
    }

    /*
     *  Get the current state of a button on the mouse
     *
     *  @param key  The button to be tested
     *  @return     If the passed button is pressed
     */
    public static boolean isButtonPressed(int button) {
        if(button < 0 || button >= 64) return false;
        return buttons[button];
    }
}
