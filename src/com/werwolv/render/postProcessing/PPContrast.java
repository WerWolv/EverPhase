package com.werwolv.render.postProcessing;

import com.werwolv.shader.ShaderContrast;

public class PPContrast extends PostProcessEffect<ShaderContrast> {

    private float contrast;

    public PPContrast(float contrast) {
        super(new ShaderContrast());

        this.contrast = contrast;
    }

    @Override
    public void render(int texture) {
        super.render(texture);

        this.getShader().start();
        this.getShader().loadContrast(contrast);
        this.getShader().stop();
    }
}
