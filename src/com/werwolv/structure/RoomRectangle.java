package com.werwolv.structure;

import com.werwolv.entity.Entity;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.RendererMaster;
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
        for(int x=0; x<length; x++){
            for(int z=0; z<width; z++) {
                blocks.add(new Entity(loader, "block", "white", new Vector3f(x+this.x, this.y, -z+this.z), new Vector3f(0, 0, 0), 0.025F));
                //blocks.add(new Entity(loader, "block", "white", new Vector3f(x+this.x, height+this.y, -z+this.z), new Vector3f(0, 0, 0), 0.025F));
                if(x==0||x==length-1||z==0||z==width-1){
                    for(int y=1; y<height; y++) {
                        blocks.add(new Entity(loader, "block", "white", new Vector3f(x+this.x, y+this.y, -z+this.z), new Vector3f(0, 0, 0), 0.025F));
                    }
                }
            }
        }
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
