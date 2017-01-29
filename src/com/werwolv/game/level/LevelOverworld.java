package com.werwolv.game.level;

import com.werwolv.game.api.event.EventBus;
import com.werwolv.game.api.event.player.OpenGuiEvent;
import com.werwolv.game.audio.SoundSource;
import com.werwolv.game.callback.CursorPositionCallback;
import com.werwolv.game.callback.KeyCallback;
import com.werwolv.game.entity.Entity;
import com.werwolv.game.entity.EntityLight;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.fbo.FrameBufferObject;
import com.werwolv.game.font.FontType;
import com.werwolv.game.gui.GuiText;
import com.werwolv.game.gui.Gui;
import com.werwolv.game.gui.GuiIngame;
import com.werwolv.game.gui.GuiInventory;
import com.werwolv.game.main.Main;
import com.werwolv.game.render.postProcessing.*;
import com.werwolv.game.resource.TextureTerrainPack;
import com.werwolv.game.structure.Labyrinth;
import com.werwolv.game.terrain.Terrain;
import com.werwolv.game.terrain.TileWater;
import com.werwolv.game.toolbox.TextRenderingHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class LevelOverworld extends Level {

    private Entity entity, entityNm;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;

    private Labyrinth labyrinth;

    private Gui guiIngame, guiInventory;

    private EntityLight entitySun = new EntityLight(new Vector3f(1000, 1000, -1000), new Vector3f(1, 0.9F, 0.9F), new Vector3f(1, 0, 0));

    private List<Gui> currentGui = new ArrayList<>();

    private FrameBufferObject postProcessing = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1]);
    private FrameBufferObject outputFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);

    private FontType font = new FontType(loader.loadGuiTexture("fonts/sans").getTextureID(), new File("res/fonts/sans.fnt"));

    private GuiText text;

    public LevelOverworld(EntityPlayer player) {
        super(player);
    }

    @Override
    public void initLevel() {
        lights.add(entitySun);
        entity = new Entity(loader, "dragon", "crate", new Vector3f(10, 0, -10), new Vector3f(0, 60, 0), 1, false);
        entityNm = new Entity(loader, "crate", "crate", new Vector3f(20, 20, -10), new Vector3f(0, 60, 0), 0.03F, true);

        entity.getModel().getTexture().setReflectivity(1.0F);
        entity.getModel().getTexture().setShineDamper(3);

        entityNm.getModel().getTexture().setReflectivity(0.5F);
        entityNm.getModel().getTexture().setShineDamper(10);
        entityNm.getModel().getTexture().setNormalMapID(loader.loadTexture("crateNormal"));
        entityNm.getModel().getTexture().setExtraInfoMapID(loader.loadTexture("crateSpecular"));

        textureTerrainPack = new TextureTerrainPack(loader,"grassy", "dirt", "path", "pinkFlowers", "blendMap");

        terrain = new Terrain(0, -1, loader, textureTerrainPack, "heightmap");

        labyrinth = new Labyrinth(loader, 0, 0, 0, 3, 10);


        entities.add(entity);
        entitiesNM.add(entityNm);

        Random random = new Random();
        for (int i = 0; i < 127; i++) {
            int x = random.nextInt(250);
            int z = -random.nextInt(250);
            entities.add(new Entity(loader, "pine", "pine", new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), new Vector3f(0, 0, 0), 1, false));
        }

        labyrinth.process();
        entities.addAll(labyrinth.RenderLabyrinth());

        terrains.add(terrain);

        waters.add(new TileWater(renderer, this, 75, -75, 0));

        guiIngame = new GuiIngame(renderer, 0, new Vector2f(0.85F, 0.5F), new Vector2f(0, 0));
        guiInventory = new GuiInventory(renderer, renderer.getShadowMapTexture(), new Vector2f(0, 0), new Vector2f(1, 1));
        //guis.add(guiIngame);

        currentGui.add(null);

        TextRenderingHelper.initTextRendering(loader);

        text = new GuiText("Hello World! Hopefully...", 1, font, new Vector2f(0.5F, 0.5F), 0.5F, true);
    }

    @Override
    public void updateLevel() {
        glfwPollEvents();
    }

    @Override
    public void renderLevel() {
        entity.increaseRotation(0, 0.5F, 0);
        renderer.renderShadowMap(entities, entitiesNM, entitySun);

        for (TileWater water : waters)
           water.renderWaterEffects();

        postProcessing.bindFrameBuffer();

        renderer.renderScene(entities, entitiesNM, terrains, lights, player, new Vector4f(0, -1, 0, 100000));
        renderer.getRendererWater().render(waters, lights, player);

        postProcessing.unbindFrameBuffer();

        postProcessing.resolveToFBO(outputFBO);
        PostProcessing.doPostProcessing(outputFBO.getColorTexture());

    }

    @Override
    public void renderGUI() {

        for (Gui gui : guis)
            renderer.getRendererGui().render(gui);

        if (player.getCurrentGui() != null) {
            renderer.getRendererGui().render(player.getCurrentGui());
        }

        TextRenderingHelper.renderTexts();

    }

    @Override
    public void handleInput() {
        super.handleInput();

        if (KeyCallback.isKeyPressedEdge(GLFW_KEY_E)) {
            if (player.getCurrentGui() != null) {
                player.setCurrentGui(null);
                Main.setCursorVisibility(false);
                CursorPositionCallback.enableCursorListener(true);
                new SoundSource("random", 1.0F, 1.0F, false).setLooping(true).setPosition(0,0,0).setVelocity(1,1,1).play();
            } else {
                EventBus.postEvent(new OpenGuiEvent(player, guiInventory));
                Main.setCursorVisibility(true);
                CursorPositionCallback.enableCursorListener(false);
            }
        }

        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F)) {
            player.toggleFlight();
            TextRenderingHelper.removeText(text);
        }

        if (player.getCurrentGui() == null) {
            player.onMove(terrain);
            player.onInteract();
        }
    }

    public void clean() {
        super.clean();

        postProcessing.clean();
        outputFBO.clean();
        PostProcessing.clean();
        TextRenderingHelper.clean();
//        AudioHelper.clean();
    }
}
