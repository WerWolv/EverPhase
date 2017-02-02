package com.deltabase.everphase.callback;


import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.input.MouseButtonPressEvent;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseButtonCallback extends GLFWMouseButtonCallback {

    private static boolean[] buttons = new boolean[64];     //Array to store each and every button on the mouse
    private static boolean[] lastButtons = new boolean[64];  //Array to store the state of each and every button on the Keyboard

    /**
     *  Get the current state of a button on the mouse
     *
     *  @param button  The button to be tested
     *  @return     If the passed button is pressed
     */
    public static boolean isButtonPressed(int button) {
        return !(button < 0 || button >= 64) && buttons[button];
    }

    public static boolean isButtonPressedEdge(int button) {
        if (button < 0 || button >= 64) return false;    //Abort if the passed key is out of bounds

        boolean currentButton = MouseButtonCallback.isButtonPressed(button);
        boolean result = currentButton && !lastButtons[button];
        lastButtons[button] = currentButton;

        return result;
    }

    /**
     * Called when any button on the mouse got pressed
     *
     * @param window    The id of the current window
     * @param button    The currently pressed button
     * @param action    If the key got pressed, released or repeated
     * @param mods      Which modifier key got held down (CTRL, SHIFT, ALT)
     */
    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;

        EventBus.postEvent(new MouseButtonPressEvent(button));
    }
}
