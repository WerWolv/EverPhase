package com.deltabase.everphase.engine.model;

import com.deltabase.everphase.engine.resource.TextureModel;

public class ModelTextured {

    private ModelRaw modelRaw;          //The model of this model
    private TextureModel texture;       //The texture of this model

    public ModelTextured(ModelRaw model, TextureModel texture) {
        this.modelRaw = model;
        this.texture = texture;
    }

    /* Getters */

    public ModelRaw getModelRaw() {
        return modelRaw;
    }

    public TextureModel getTexture() {
        return texture;
    }

}
