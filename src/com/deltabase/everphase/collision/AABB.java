package com.deltabase.everphase.collision;

import org.joml.Vector3f;

public class AABB {

    private Vector3f center = new Vector3f(0.0F, 0.0F, 0.0F);
    private Vector3f extent = new Vector3f(0.0F, 0.0F, 0.0F);

    public AABB(Vector3f center, Vector3f size) {
        this.center = center;
        this.extent = size;
    }

    public boolean intersectsWith(AABB other) {
        if (this.center.x - this.extent.x <= other.center.x + other.extent.x && this.center.x + this.extent.x >= other.center.x - other.extent.x)
            if (this.center.z - this.extent.z <= other.center.z + other.extent.z && this.center.z + this.extent.z >= other.center.z - other.extent.z)
                return true;

        return false;
    }

    public Vector3f getCenter() {
        return center;
    }

    public Vector3f getExtent() {
        return extent;
    }

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
