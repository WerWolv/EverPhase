package com.deltabase.everphase.entity;

import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.player.OpenGuiEvent;
import com.deltabase.everphase.api.event.player.PlayerItemUseEvent;
import com.deltabase.everphase.api.event.player.PlayerMoveEvent;
import com.deltabase.everphase.callback.CursorPositionCallback;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.callback.MouseButtonCallback;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.item.ItemStack;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.skill.Skill;
import com.deltabase.everphase.skill.SkillAttack;
import com.deltabase.everphase.terrain.Terrain;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;


public class EntityPlayer extends Entity{

    private static final float PLAYER_HEIGHT = 6.0F;    //Height of the player to render the camera above the ground.

    private int selectedItem = 0;

    private float speedX, speedY, speedZ;               //Speed of the player in different directions.
    private boolean isInAir = false;                    //Is the player currently in the air?
    private boolean canFly = false;

    private float pitch;    //The horizontal angle of the camera
    private float yaw;      //The vertical angle of the camera
    private float roll;     //The longitudinal angle of the camera

    private Gui currentGui;

    private ItemStack heldItem;

    private List<Skill> skills = new ArrayList<>();

    public EntityPlayer(ResourceLoader loader, Vector3f position, Vector3f rotation, float scale) {
        super(loader, "", "", position, rotation, scale, false);

        skills.add(new SkillAttack("Attack", loader.loadGuiTexture("gui/skills").getTextureID(), 99));
    }

    public void update() {

    }

    public void onXPEarned() {
        for (Skill skill : skills) {
            if (skill.getCurrentXP() >= skill.getXPToNextLevel(skill.getCurrentLevel())) {
                skill.levelUp();
                skill.onLevelUp();
            }
        }
    }

    public void onMove(Terrain terrain) {
        speedX = speedZ = 0;    //Reset the speed of the player

        speedY += canFly ? -speedY : GRAVITY * Main.getFrameTimeSeconds();      //Add gravity to the player to keep it on the ground

        //Keybindings to move the player in different directions based of the pressed buttons and the direction of the camera
        if (KeyCallback.isKeyPressed(GLFW_KEY_W)) {
            speedX += 40F * (float) Math.sin(Math.toRadians(getYaw()));
            speedZ -= 40F * (float) Math.cos(Math.toRadians(getYaw()));
        }
        if (KeyCallback.isKeyPressed(GLFW_KEY_S)) {
            speedX -= 40F * (float) Math.sin(Math.toRadians(getYaw()));
            speedZ += 40F * (float) Math.cos(Math.toRadians(getYaw()));
        }
        if (KeyCallback.isKeyPressed(GLFW_KEY_A)) {
            speedX -= 40F * (float) Math.cos(Math.toRadians(getYaw()));
            speedZ -= 40F * (float) Math.sin(Math.toRadians(getYaw()));
        }
        if (KeyCallback.isKeyPressed(GLFW_KEY_D)) {
            speedX += 40F * (float) Math.cos(Math.toRadians(getYaw()));
            speedZ += 40F * (float) Math.sin(Math.toRadians(getYaw()));
        }
        if(canFly) {
            if(KeyCallback.isKeyPressed(GLFW_KEY_SPACE))
                speedY += 40F;

            if (KeyCallback.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
                speedY -= 40F;
        } else {
            if (KeyCallback.isKeyPressed(GLFW_KEY_SPACE) && !isInAir) {
                speedY += 30F;
                isInAir = true;
            }
        }

        EventBus.postEvent(new PlayerMoveEvent(this, position, new Vector3f(speedX, speedY, speedZ), new Vector3f(pitch, yaw, roll)));

        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z) + PLAYER_HEIGHT;   //Get the height of the terrain at the current position of the player
        if(position.y < terrainHeight) {        //If the player is under the terrain plane...
            speedY = 0;                         //...reset the downwards speed
            isInAir = false;                    //...set the player not in the air anymore
            position.y = terrainHeight;         //...and set the y position of the player to the height of the the terrain
        }
    }

    public void onInteract() {
        PlayerItemUseEvent.Action currAction = PlayerItemUseEvent.Action.NONE;
        if(MouseButtonCallback.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            if(KeyCallback.isKeyPressed(GLFW_MOD_SHIFT)) currAction = PlayerItemUseEvent.Action.SHIFT_LEFT_CLICK;
            else if(KeyCallback.isKeyPressed(GLFW_MOD_CONTROL)) currAction = PlayerItemUseEvent.Action.CTRL_LEFT_CLICK;
            else currAction = PlayerItemUseEvent.Action.LEFT_CLICK;
        } else if(MouseButtonCallback.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            if(KeyCallback.isKeyPressed(GLFW_MOD_SHIFT)) currAction = PlayerItemUseEvent.Action.SHIFT_RIGHT_CLICK;
            else if(KeyCallback.isKeyPressed(GLFW_MOD_CONTROL)) currAction = PlayerItemUseEvent.Action.CTRL_RIGHT_CLICK;
            else currAction = PlayerItemUseEvent.Action.RIGHT_CLICK;
        }

        if(this.getHeldItem() != null && currAction != PlayerItemUseEvent.Action.NONE)
            this.heldItem = this.getHeldItem().getItem().onItemClick(this.getHeldItem(), this, currAction);

    }

    /* Getters and Setters */

    public void addPosition(float x, float y, float z) {
        position.x += x * Main.getFrameTimeSeconds();
        position.y += y * Main.getFrameTimeSeconds();
        position.z += z * Main.getFrameTimeSeconds();
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

    public Gui getCurrentGui() {
        return currentGui;
    }

    public void setCurrentGui(Gui currentGui) {
        if (currentGui != null) {
            EventBus.postEvent(new OpenGuiEvent(this, currentGui));
            Main.setCursorVisibility(true);
            CursorPositionCallback.enableCursorListener(false);
        } else {
            Main.setCursorVisibility(false);
            CursorPositionCallback.enableCursorListener(true);
        }

        this.currentGui = currentGui;
    }

    public Skill getSkill(Class skillClass) {
        for (Skill skill : skills)
            if (skillClass.isInstance(skill))
                return skill;
        return null;
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public int getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedItem(int index) {
        this.selectedItem = index;
    }

    public void toggleFlight() {
        this.canFly = !canFly;
    }
}
