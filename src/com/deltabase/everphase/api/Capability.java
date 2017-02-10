package com.deltabase.everphase.api;

public class Capability {

    private float value;
    private float maxValue;

    public Capability(float value, float maxValue) {
        this.value = value;
        this.maxValue = maxValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
