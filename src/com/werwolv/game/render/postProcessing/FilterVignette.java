package com.werwolv.game.render.postProcessing;

import com.werwolv.game.main.Main;
import com.werwolv.game.shader.filter.ShaderVignette;

public class FilterVignette extends Filter<ShaderVignette> {

    public FilterVignette() {
        super(new ShaderVignette(), Main.getWindowSize()[0], Main.getWindowSize()[1]);

        getShader().start();
        getShader().loadResolution(Main.getWindowSize()[0], Main.getWindowSize()[1]);
        getShader().stop();
    }

}
