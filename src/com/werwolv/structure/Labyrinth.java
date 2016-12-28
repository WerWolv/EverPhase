package com.werwolv.structure;

import com.werwolv.entity.Entity;
import com.werwolv.render.ModelLoader;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Labyrinth {

    //Settings
    private int room_length = 10, room_width = 10, room_height = 2;

    //Defines
    private ModelLoader loader;

    private List<RoomRectangle> rooms = new ArrayList<>();
    private List<Entity> blocks = new ArrayList<>();

    Random random = new Random();

    private int pos_x;
    private int pos_y;
    private int pos_z;

    public Labyrinth(ModelLoader loader, int x, int y, int z){
        this.loader = loader;
        this.pos_x = x;
        this.pos_y = y;
        this.pos_z = z;
    }

    public void process(){
        int x = random.nextInt(1);
        int z = random.nextInt(1);
        room(x, z);
        for(int b = 0; b < 1; b++){
            int index = random.nextInt(rooms.size());
            x = (int)rooms.get(index).get_x();
            z = (int)rooms.get(index).get_z();
            for(int a = 0; a < 10; a++){
                int direction = (random.nextInt(4) + 1);
                /*while(true){
                    if((z == 0)&&(direction == 1)) direction = (random.nextInt(4) + 1);
                    else if((x == 0)&&(direction == 2)) direction = (random.nextInt(4) + 1);
                    else if((z ==  pos_y)&&(direction == 4)) direction = (random.nextInt(4) + 1);
                    else if((x ==  pos_x)&&(direction == 3))direction = (random.nextInt(4) + 1);
                    else break;
                }*/
                switch (direction) {
                    case 1:
                        z -= 1;
                        break;

                    case 2:
                        x -= 1;
                        break;

                    case 3:
                        x += 1;
                        break;

                    case 4:
                        z += 1;
                        break;

                    default:
                        break;
                }

                room(x, z);
            }
        }

        for(RoomRectangle room : rooms){
            room.process();
            blocks.addAll(room.RenderRoom());
        }
        rooms.clear();
    }

    private void room(int x, int z) {
        rooms.add(new RoomRectangle(loader, x*(room_length-1), 0, z*(room_width-1), room_length, room_width, room_height));
    }

    public List<Entity> RenderLabyrinth(){
        return blocks;
    }
}
