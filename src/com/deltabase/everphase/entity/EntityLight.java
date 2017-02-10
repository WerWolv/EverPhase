package com.deltabase.everphase.entity;

import org.joml.Vector3f;

public class EntityLight extends Entity {

    private Vector3f color;     //The color of the light
    private Vector3f attenuation = new Vector3f(1, 0, 0);   //The intensity of the light (How fast the light decreses over distance)


    public EntityLight(Vector3f color) {
        super(0.0F, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        this.color = color;
    }

    public EntityLight(Vector3f color, Vector3f attenuation) {
        super(0.0F, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        this.color = color;
        this.attenuation = attenuation;
    }

    /* Getters and Setters */

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }
}
