package com.werwolv.structure;

import com.werwolv.entity.Entity;
import com.werwolv.modelloader.ModelLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Labyrinth {
    private Random random = new Random();
    //Settings
    private int room_length = 18, room_width = 18, room_height = 2;
    //Defines
    private ModelLoader loader;
    private List<RoomRectangle> rooms = new ArrayList<>();
    private List<Entity> blocks = new ArrayList<>();
    private int[][] labyrinth = new int[30][30];
    private boolean test = true;
    private int old_index = 0;
    private int pos_x;
    private int pos_y;
    private int pos_z;
    private int length;
    private int branch;

    public Labyrinth(ModelLoader loader, int x, int y, int z, int branch, int length){
        this.loader = loader;
        this.pos_x = x;
        this.pos_y = y;
        this.pos_z = z;
        this.length = length;
        this.branch = branch;
    }

    public void process(){
        int old_direction;
        int x = random.nextInt(1);
        int z = random.nextInt(1);
        room(x, z);
        for(int b = 0; b < branch; b++){
            int index = random.nextInt(rooms.size());
            x = (int)rooms.get(index).get_x() / room_length;
            z = (int)rooms.get(index).get_z() / room_width;
            old_direction = 0;
            if(b>0){
                test = false;
                old_index = index;
            }
            int direction;
            for(int a = 0; a < length; a++){
                direction = (random.nextInt(4) + 1);
                while(true){
                    if((z == -15)&&(direction == 1)) direction = (random.nextInt(4) + 1);
                    else if((x == -15)&&(direction == 2)) direction = (random.nextInt(4) + 1);
                    else if((z ==  14)&&(direction == 4)) direction = (random.nextInt(4) + 1);
                    else if((x ==  14)&&(direction == 3))direction = (random.nextInt(4) + 1);
                    else break;
                }
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
                if(test){
                    door(old_direction, direction, rooms.size() - 1);
                }else{
                    door(old_direction, direction, old_index);
                }
                if(labyrinth[x+15][z+15]==0) {
                    room(x, z);
                    test = true;
                }else{
                    //door(direction, 0, labyrinth[x+15][z+15]-1);
                    old_index = labyrinth[x+15][z+15]-1;
                    test = false;
                }
                old_direction = direction;
            }
            if(test){
                door(old_direction, 0, rooms.size()-1);
            }else{
                door(old_direction, 0, old_index);
            }
        }

        for(RoomRectangle room : rooms){
            room.process();
            blocks.addAll(room.RenderRoom());
        }
        rooms.clear();
    }

    private void room(int x, int z) {
        rooms.add(new RoomRectangle(loader, x*(room_length), 0, z*(room_width), room_length, room_width, room_height));
        labyrinth[x+15][z+15] = rooms.size();
    }

    private void door(int old_d, int new_d, int index){
        if(old_d==4||new_d==1) rooms.get(index).set_Doors(false, true, false, false);
        if(old_d==3||new_d==2) rooms.get(index).set_Doors(false, false, false, true);
        if(old_d==2||new_d==3) rooms.get(index).set_Doors(false, false, true, false);
        if(old_d==1||new_d==4) rooms.get(index).set_Doors(true, false, false, false);

    }

    public List<Entity> RenderLabyrinth(){
        return blocks;
    }
}
