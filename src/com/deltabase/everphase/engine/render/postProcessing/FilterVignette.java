package com.deltabase.everphase.engine.render.postProcessing;

import com.deltabase.everphase.engine.shader.filter.ShaderVignette;
import com.deltabase.everphase.main.Main;

public class FilterVignette extends Filter<ShaderVignette> {

    public FilterVignette() {
        super(new ShaderVignette(), Main.getWindowSize()[0], Main.getWindowSize()[1]);

        getShader().start();
        getShader().loadResolution(Main.getWindowSize()[0], Main.getWindowSize()[1]);
        getShader().stop();
    }

}
