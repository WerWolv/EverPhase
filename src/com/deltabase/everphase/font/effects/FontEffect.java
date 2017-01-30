package com.deltabase.everphase.font.effects;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontEffect {

    private float charWidth, charEdgeWidth;
    private float charBorderWidth, charBorderEdgeWidth;
    private Vector2f offset;
    private Vector3f color;

    public FontEffect(float charWidth, float charEdgeWidth, float charBorderWidth, float charBorderEdgeWidth, Vector2f offset, Vector3f color) {
        this.charWidth = charWidth;
        this.charEdgeWidth = charEdgeWidth;
        this.charBorderWidth = charBorderWidth;
        this.charBorderEdgeWidth = charBorderEdgeWidth;
        this.offset = offset;
        this.color = color;
    }

    public FontEffect() {
        this.charWidth = 0.5F;
        this.charEdgeWidth = 0.1F;
        this.charBorderWidth = 0.0F;
        this.charBorderEdgeWidth = 0.4F;
        this.offset = new Vector2f(0.0F, 0.0F);
        this.color = new Vector3f(0.0F, 0.0F, 0.0F);
    }

    public float getCharWidth() {
        return charWidth;
    }

    public void setCharWidth(float charWidth) {
        this.charWidth = charWidth;
    }

    public float getCharEdgeWidth() {
        return charEdgeWidth;
    }

    public void setCharEdgeWidth(float charEdgeWidth) {
        this.charEdgeWidth = charEdgeWidth;
    }

    public float getCharBorderWidth() {
        return charBorderWidth;
    }

    public void setCharBorderWidth(float charBorderWidth) {
        this.charBorderWidth = charBorderWidth;
    }

    public float getCharBorderEdgeWidth() {
        return charBorderEdgeWidth;
    }

    public void setCharBorderEdgeWidth(float charBorderEdgeWidth) {
        this.charBorderEdgeWidth = charBorderEdgeWidth;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
