package com.werwolv.game.callback;

import com.werwolv.game.api.event.EventBus;
import com.werwolv.game.api.event.input.ScrollEvent;
import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollCallback extends GLFWScrollCallback {

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        EventBus.postEvent(new ScrollEvent((int) xoffset, (int) yoffset));
    }

}
