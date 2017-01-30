package com.deltabase.everphase.render.postProcessing;

import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.shader.filter.ShaderVignette;

public class FilterVignette extends Filter<ShaderVignette> {

    public FilterVignette() {
        super(new ShaderVignette(), Main.getWindowSize()[0], Main.getWindowSize()[1]);

        getShader().start();
        getShader().loadResolution(Main.getWindowSize()[0], Main.getWindowSize()[1]);
        getShader().stop();
    }

}
