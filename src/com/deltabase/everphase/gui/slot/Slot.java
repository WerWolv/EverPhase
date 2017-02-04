package com.deltabase.everphase.gui.slot;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.event.inventory.InventoryItemClickEvent;
import com.deltabase.everphase.api.event.inventory.InventoryItemHoverEvent;
import com.deltabase.everphase.callback.CursorPositionCallback;
import com.deltabase.everphase.callback.MouseButtonCallback;
import com.deltabase.everphase.inventory.Inventory;
import com.deltabase.everphase.item.ItemStack;
import com.deltabase.everphase.main.Main;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Slot {

    public static final float SLOT_SIZE = 0.07F;

    private Inventory inventory;
    private Vector2f position;
    private ItemStack itemStack;
    private int maxStackSize;
    private int slotID;

    public Slot(Inventory inventory, Vector2f position, int maxStackSize) {
        this.inventory = inventory;
        this.position = position;
        this.maxStackSize = maxStackSize;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public boolean isMouseOverSlot() {
        double cursorX = ((CursorPositionCallback.xPos / Main.getWindowSize()[0]) * 2.0F - 1.0F);
        double cursorY = -((CursorPositionCallback.yPos / Main.getWindowSize()[1]) * 2.0F - 1.0F);

        boolean isHovering = (cursorX > (getPosition().x() - (Slot.SLOT_SIZE / Main.getAspectRatio())) && cursorX < (getPosition().x() + (Slot.SLOT_SIZE / Main.getAspectRatio())) && cursorY > (getPosition().y() - (Slot.SLOT_SIZE)) && cursorY < (getPosition().y() + (Slot.SLOT_SIZE)));

        if (isHovering) {
            EverPhaseApi.EVENT_BUS.postEvent(new InventoryItemHoverEvent(getItemStack(), this, ((float) cursorX + 1.0F) / 2.0F, ((float) -cursorY + 1.0F) / 2.0F));

            if (MouseButtonCallback.isButtonPressedEdge(GLFW.GLFW_MOUSE_BUTTON_LEFT))
                EverPhaseApi.EVENT_BUS.postEvent(new InventoryItemClickEvent(inventory, getItemStack(), this, InventoryItemClickEvent.MOUSE_BUTTON_LEFT));
            if (MouseButtonCallback.isButtonPressedEdge(GLFW.GLFW_MOUSE_BUTTON_MIDDLE))
                EverPhaseApi.EVENT_BUS.postEvent(new InventoryItemClickEvent(inventory, getItemStack(), this, InventoryItemClickEvent.MOUSE_BUTTON_MIDDLE));
            if (MouseButtonCallback.isButtonPressedEdge(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
                EverPhaseApi.EVENT_BUS.postEvent(new InventoryItemClickEvent(inventory, getItemStack(), this, InventoryItemClickEvent.MOUSE_BUTTON_RIGHT));
        }

        return isHovering;
    }
}
