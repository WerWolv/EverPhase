package com.werwolv.entity;

import com.werwolv.input.KeyListener;
import com.werwolv.terrain.Terrain;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;


public class EntityPlayer extends Entity{

    private static final float GRAVITY = -0.03F;        //Gravity constance. Used for jumping in the moment
    private static final float PLAYER_HEIGHT = 4.0F;    //Height of the player to render the camera above the ground.

    private float speedX, speedY, speedZ;               //Speed of the player in different directions.
    private boolean isInAir = false;                    //Is the player currently in the air?

    private float pitch;    //The horizontal angle of the camera
    private float yaw;      //The vertical angle of the camera
    private float roll;     //The longitudinal angle of the camera

    public EntityPlayer(Vector3f position, Vector3f rotation, float scale) {
        super(null, "", "", position, rotation, scale);
    }


    public void move(Terrain terrain) {
        speedX = speedZ = 0;    //Reset the speed of the player

        speedY += GRAVITY;      //Add gravity to the player to keep it on the ground

        //Keybindings to move the player in different directions based of the pressed buttons and the direction of the camera
        if(KeyListener.isKeyPressed(GLFW_KEY_W)) { speedX = 0.4F * (float)Math.sin(Math.toRadians(getYaw())); speedZ = -0.4F * (float)Math.cos(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)) { speedX = -0.4F * (float)Math.sin(Math.toRadians(getYaw())); speedZ = 0.4F * (float)Math.cos(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_A)) { speedX = -0.4F * (float)Math.cos(Math.toRadians(getYaw())); speedZ = -0.4F * (float)Math.sin(Math.toRadians(getYaw())); }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)) { speedX = 0.4F * (float)Math.cos(Math.toRadians(getYaw())); speedZ = 0.4F * (float)Math.sin(Math.toRadians(getYaw())); }

        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !isInAir) {
            speedY = 0.5F;      //Add vertical speed to the player
            isInAir = true;     //The player is now in the air
        }

        addPosition(speedX, speedY, speedZ);    //Add the speeds calculated above to the player

        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z) + PLAYER_HEIGHT;   //Get the height of the terrain at the current position of the player
        if(position.y < terrainHeight) {        //If the player is under the terrain plane...
            speedY = 0;                         //...reset the downwards speed
            isInAir = false;                    //...set the player not in the air anymore
            position.y = terrainHeight;         //...and set the y position of the player to the height of the the terrain
        }
    }

    /* Getters and Setters */

    public void addPosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
