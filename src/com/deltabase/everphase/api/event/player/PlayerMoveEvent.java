package com.deltabase.everphase.api.event.player;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityPlayer;
import org.joml.Vector3f;

public class PlayerMoveEvent extends Event {

    private EntityPlayer player;
    private Vector3f coords, speed, rotation;

    public PlayerMoveEvent(EntityPlayer player, Vector3f coords, Vector3f speed, Vector3f rotation) {
        this.player = player;
        this.coords = coords;
        this.speed = speed;
        this.rotation = rotation;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Vector3f getCoords() {
        return coords;
    }

    public Vector3f getSpeed() {
        return speed;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
