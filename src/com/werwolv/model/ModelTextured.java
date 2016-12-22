package com.werwolv.model;

import com.werwolv.resource.TextureModel;

public class ModelTextured {

    private ModelRaw modelRaw;
    private TextureModel texture;

    public ModelTextured(ModelRaw model, TextureModel texture) {
        this.modelRaw = model;
        this.texture = texture;
    }

    public ModelRaw getModelRaw() {
        return modelRaw;
    }

    public TextureModel getTexture() {
        return texture;
    }

}
