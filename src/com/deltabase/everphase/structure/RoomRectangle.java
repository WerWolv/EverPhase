package com.deltabase.everphase.structure;

import com.deltabase.everphase.entity.Entity;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class RoomRectangle {
    private List<Entity> blocks = new ArrayList<>();


    private int length;
    private int width;
    private int height;

    private int x;
    private int y;
    private int z;

    private boolean north=false,  south=false,  west=false,  east=false;

    public RoomRectangle(int x, int y, int z, int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void process(){
        blocks.add(new Entity("labyrinth/floor", "models/labyrinth/floor", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x, this.y, this.z)));

        if (north)
            blocks.add(new Entity("labyrinth/door1", "models/labyrinth/door1", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x, this.y + 1, this.z + 8.5F)));
        else
            blocks.add(new Entity("labyrinth/wall1", "models/labyrinth/wall1", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x, this.y + 1, this.z + 8.5F)));

        if (south)
            blocks.add(new Entity("labyrinth/door1", "models/labyrinth/door1", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x, this.y + 1, this.z - 8.5F)));
        else
            blocks.add(new Entity("labyrinth/wall1", "models/labyrinth/wall1", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x, this.y + 1, this.z - 8.5F)));

        if (west)
            blocks.add(new Entity("labyrinth/door2", "models/labyrinth/door2", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x + 8.5F, this.y + 1, this.z)));
        else
            blocks.add(new Entity("labyrinth/wall2", "models/labyrinth/wall2", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x + 8.5F, this.y + 1, this.z)));

        if (east)
            blocks.add(new Entity("labyrinth/door2", "models/labyrinth/door2", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x - 8.5F, this.y + 1, this.z)));
        else
            blocks.add(new Entity("labyrinth/wall2", "models/labyrinth/wall2", new Vector3f(0, 0, 0), 1F, false).setPosition(new Vector3f(this.x - 8.5F, this.y + 1, this.z)));
    }

    public void set_Doors(boolean north, boolean south, boolean west, boolean east){
        if(north) this.north = true;
        if(south) this.south = true;
        if(west) this.west = true;
        if(east) this.east = true;
    }

    public List<Entity> RenderRoom(){
        return blocks;
    }

    public float get_x() {
        return x;
    }

    public float get_y() {
        return y;
    }

    public float get_z() {
        return z;
    }
}
