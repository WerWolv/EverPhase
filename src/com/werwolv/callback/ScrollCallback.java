package com.werwolv.callback;

import com.werwolv.api.event.EventBus;
import com.werwolv.api.event.input.ScrollEvent;
import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollCallback extends GLFWScrollCallback {

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        EventBus.postEvent(new ScrollEvent((int) xoffset, (int) yoffset));
    }

}
