package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.callback.CursorPositionCallback;
import com.deltabase.everphase.engine.model.ModelRaw;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.engine.shader.ShaderGui;
import com.deltabase.everphase.engine.toolbox.Maths;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.inventory.GuiInventory;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.main.Main;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class RendererGui {

    private final ModelRaw quad;
    private ShaderGui shader;
    private ResourceLoader loader;

    private List<TextureGui> textureUnits = new ArrayList<>();

    public RendererGui(ResourceLoader loader) {
        float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1};  //The six indices of the quad
        quad = loader.loadToVAO(positions, 2);   //Create the quad out of the six vertices

        this.loader = loader;
        shader = new ShaderGui();                           //Create an instance of the GUI Shader
    }

    public void render(Gui gui) {
        shader.start();                                                     //Start the shader
        RendererMaster.disableCulling();
        GL30.glBindVertexArray(quad.getVaoID());                            //Bind the VAO of the quad to memory
        GL20.glEnableVertexAttribArray(0);                            //Enable the vertices buffer
        GL11.glEnable(GL11.GL_BLEND);                                       //Enable transparency
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);                                 //Disable the depth test so you can render multiple transparent GUIs behind each other

        gui.render();

        if (gui instanceof GuiInventory) {
            GuiInventory guiInventory = (GuiInventory) gui;

            for (Slot slot : guiInventory.getInventory().inventorySlots)
                drawSlot(slot);
        }


        GL11.glEnable(GL11.GL_DEPTH_TEST);                                  //Enable the depth test again
        GL11.glDisable(GL11.GL_BLEND);                                      //Disable transparency
        GL20.glDisableVertexAttribArray(0);                          //Disable the vertices buffer
        GL30.glBindVertexArray(0);                           //Unbind the VAO
        shader.loadSize(0.0F, 0.0F, 1.0F, 1.0F);
        RendererMaster.enableCulling();
        shader.stop();                                                      //Stop the shader

        textureUnits.clear();
    }

    public void drawSlot(Slot slot) {
        double cursorX = (CursorPositionCallback.xPos / Main.getWindowSize()[0]) * 2.0F - 1.0F;
        double cursorY = -((CursorPositionCallback.yPos / Main.getWindowSize()[1]) * 2.0F - 1.0F);

        if (slot.getItemStack() == null) return;

        if (cursorX > (slot.getPosition().x() - (Slot.SLOT_SIZE / 2.0F) - 0.075F / Main.getAspectRatio()) && cursorX < (slot.getPosition().x() + (Slot.SLOT_SIZE / 2.0F) + 0.075F / Main.getAspectRatio()) && cursorY > (slot.getPosition().y() - (Slot.SLOT_SIZE / 2.0F) - 0.125F) && cursorY < (slot.getPosition().y() + (Slot.SLOT_SIZE / 2.0F) + 0.125F))
            return;

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, slot.getItemStack().getItem().getTextureID());
        shader.loadTransformationMatrix(Maths.createTransformationMatrix(slot.getPosition(), new Vector2f(Math.min(Slot.SLOT_SIZE, Slot.SLOT_SIZE / Main.getAspectRatio()), -Math.min(Slot.SLOT_SIZE, Slot.SLOT_SIZE * Main.getAspectRatio()))));
        shader.loadSize(0, 0, 64, 64);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
    }

    public void drawTexture(float posX, float posY, float scale, Vector4f size, TextureGui texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        shader.loadTransformationMatrix(Maths.createTransformationMatrix(new Vector2f(posX, posY), new Vector2f(Math.min(scale, scale / Main.getAspectRatio()), -Math.min(scale, scale * Main.getAspectRatio()))));
        shader.loadSize(size.x / texture.getSize(), size.y / texture.getSize(), size.z / texture.getSize(), size.w / texture.getSize());
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
    }

    /*
     * Cleanup the shader
     */
    public void clean() {
        shader.clean();
    }
}
