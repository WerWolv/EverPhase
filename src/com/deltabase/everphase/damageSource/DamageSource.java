package com.deltabase.everphase.damageSource;

public abstract class DamageSource {

    private String name, desc;

    public DamageSource(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
