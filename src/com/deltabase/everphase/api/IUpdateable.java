package com.deltabase.everphase.api;

public interface IUpdateable {

    void update();

    default void setUpdatable() {
        EverPhaseApi.updateables.add(this);
    }

}
