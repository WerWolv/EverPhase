package com.werwolv.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyListener extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[1024];  //Array to store the state of each and every button on the Keyboard

    /*
     * Called when any key on the keyboard got pressed
     *
     * @param window    The id of the current window
     * @param key       The currently pressed key
     * @param scancode  The scancode of the pressed key
     * @param action    If the key got pressed, released or repeated
     * @param mods      Which modifier key got held down (CTRL, SHIFT, ALT)
     */
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

    /*
     *  Get the current state of a key
     *
     *  @param key  The key to be tested
     *  @return     If the passed key is pressed
     */
    public static boolean isKeyPressed(int key) {
        if(key < 0 || key >= 1024) return false;    //Abort if the passed key is out of bounds

        return keys[key];
    }
}
