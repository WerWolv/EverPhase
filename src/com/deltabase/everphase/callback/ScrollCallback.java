package com.deltabase.everphase.callback;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.event.input.ScrollEvent;
import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollCallback extends GLFWScrollCallback {

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        EverPhaseApi.EVENT_BUS.postEvent(new ScrollEvent((int) xoffset, (int) yoffset));
    }

}
