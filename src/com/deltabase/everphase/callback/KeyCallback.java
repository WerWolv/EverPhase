package com.deltabase.everphase.callback;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.event.input.KeyPressEvent;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyCallback extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[1024];  //Array to store the state of each and every button on the Keyboard
    private static boolean[] lastKeys = new boolean[1024];  //Array to store the state of each and every button on the Keyboard

    /**
     *  Get the current state of a key
     *
     *  @param key  The key to be tested
     *  @return     If the passed key is pressed
     */
    public static boolean isKeyPressed(int key) {
        return !(key < 0 || key >= 1024) && keys[key];
    }

    public static boolean isKeyPressedEdge(int key) {
        if (key < 0 || key >= 1024) return false;    //Abort if the passed key is out of bounds

        boolean currentKey = KeyCallback.isKeyPressed(key);
        boolean result = currentKey && !lastKeys[key];
        lastKeys[key] = currentKey;

        return result;
    }

    /**
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
        if (key < 0 || key >= 1024) return;    //Abort if the passed key is out of bounds

        keys[key] = action != GLFW_RELEASE;

        EverPhaseApi.EVENT_BUS.postEvent(new KeyPressEvent(key));
    }

}
