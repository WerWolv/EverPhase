package com.werwolv.game.entity;

import org.joml.Vector3f;

public class EntityLight {

    private Vector3f position;  //The position of the light in the world
    private Vector3f color;     //The color of the light
    private Vector3f attenuation = new Vector3f(1, 0, 0);   //The intensity of the light (How fast the light decreses over distance)


    public EntityLight(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public EntityLight(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    /* Getters and Setters */

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

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
