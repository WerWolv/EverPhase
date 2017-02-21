package com.deltabase.everphase.gui.elements;

import com.deltabase.everphase.callback.CursorPositionCallback;
import com.deltabase.everphase.callback.MouseButtonCallback;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.main.Main;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class GuiButton {

    private int id;
    private Vector2f position;
    private Vector4f texturePosition;
    private String text;
    private TextureGui texture;
    private boolean hovering = false;
    private boolean mouseDown = false;

    public GuiButton(int id, Vector2f position, Vector4f texturePosition, String text, TextureGui texture) {
        this.id = id;
        this.position = position;
        this.texturePosition = texturePosition;
        this.text = text;
        this.texture = texture;
    }

    public boolean isMouseOverSlot() {
        double cursorX = ((CursorPositionCallback.xPos / Main.getWindowSize()[0]) * 2.0F - 1.0F);
        double cursorY = -((CursorPositionCallback.yPos / Main.getWindowSize()[1]) * 2.0F - 1.0F);

        boolean isHovering = (cursorX > (getPosition().x() - (texturePosition.x / Main.getAspectRatio())) && cursorX < (getPosition().x() + ((texturePosition.z - texturePosition.x) / Main.getAspectRatio())) && cursorY > (getPosition().y() - (texturePosition.y)) && cursorY < (getPosition().y() + (texturePosition.w - texturePosition.y)));

        if (isHovering) {
            hovering = true;
            mouseDown = MouseButtonCallback.isButtonPressedEdge(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        } else hovering = false;

        return isHovering;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector4f getTexturePosition() {
        return texturePosition;
    }

    public String getText() {
        return text;
    }

    public TextureGui getTexture() {
        return texture;
    }
}
