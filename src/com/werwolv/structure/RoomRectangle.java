package com.werwolv.structure;

import com.werwolv.entity.Entity;
import com.werwolv.modelloader.ModelLoader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class RoomRectangle {
    private List<Entity> blocks = new ArrayList<>();

    private ModelLoader loader;

    private int length;
    private int width;
    private int height;

    private int x;
    private int y;
    private int z;

    private boolean north=false,  south=false,  west=false,  east=false;

    public RoomRectangle(ModelLoader loader, int x, int y, int z, int length, int width, int height){
        this.loader = loader;
        this.length = length;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void process(){
        blocks.add(new Entity(loader, "labyrinth/floor", "models/labyrinth/floor", new Vector3f(this.x, this.y, this.z), new Vector3f(0, 0, 0), 1F, false));

        if(north) blocks.add(new Entity(loader, "labyrinth/door1", "models/labyrinth/door1", new Vector3f(this.x, this.y+1, this.z+8.5F), new Vector3f(0, 0, 0), 1F, false));
        else blocks.add(new Entity(loader, "labyrinth/wall1", "models/labyrinth/wall1", new Vector3f(this.x, this.y+1, this.z+8.5F), new Vector3f(0, 0, 0), 1F, false));

        if(south) blocks.add(new Entity(loader, "labyrinth/door1", "models/labyrinth/door1", new Vector3f(this.x, this.y+1, this.z-8.5F), new Vector3f(0, 0, 0), 1F, false));
        else blocks.add(new Entity(loader, "labyrinth/wall1", "models/labyrinth/wall1", new Vector3f(this.x, this.y+1, this.z-8.5F), new Vector3f(0, 0, 0), 1F, false));

        if(west) blocks.add(new Entity(loader, "labyrinth/door2", "models/labyrinth/door2", new Vector3f(this.x+8.5F, this.y+1, this.z), new Vector3f(0, 0, 0), 1F, false));
        else blocks.add(new Entity(loader, "labyrinth/wall2", "models/labyrinth/wall2", new Vector3f(this.x+8.5F, this.y+1, this.z), new Vector3f(0, 0, 0), 1F, false));

        if(east) blocks.add(new Entity(loader, "labyrinth/door2", "models/labyrinth/door2", new Vector3f(this.x-8.5F, this.y+1, this.z), new Vector3f(0, 0, 0), 1F, false));
        else blocks.add(new Entity(loader, "labyrinth/wall2", "models/labyrinth/wall2", new Vector3f(this.x-8.5F, this.y+1, this.z), new Vector3f(0, 0, 0), 1F, false));
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
