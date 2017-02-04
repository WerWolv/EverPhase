package com.deltabase.everphase.level;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.content.Achievements;
import com.deltabase.everphase.engine.audio.AudioHelper;
import com.deltabase.everphase.engine.fbo.FrameBufferObject;
import com.deltabase.everphase.engine.font.FontType;
import com.deltabase.everphase.engine.font.effects.FontEffect;
import com.deltabase.everphase.engine.render.postProcessing.PostProcessing;
import com.deltabase.everphase.engine.resource.TextureTerrainPack;
import com.deltabase.everphase.engine.toolbox.ParticleHelper;
import com.deltabase.everphase.engine.toolbox.TextRenderingHelper;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.GuiIngame;
import com.deltabase.everphase.gui.GuiMiniMap;
import com.deltabase.everphase.gui.GuiText;
import com.deltabase.everphase.gui.inventory.GuiInventoryPlayer;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.structure.Labyrinth;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.terrain.TileWater;
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

    private Gui guiIngame, guiInventory, guiMiniMap;

    private EntityLight entitySun = new EntityLight(new Vector3f(1000, 1000, -1000), new Vector3f(1, 0.9F, 0.9F), new Vector3f(1, 0, 0));

    private List<Gui> currentGui = new ArrayList<>();

    private FrameBufferObject postProcessing = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1]);
    private FrameBufferObject outputFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);
    private FrameBufferObject miniMapFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);

    private FontType font = new FontType(loader.loadGuiTexture("fonts/productSans").getTextureID(), new File("res/fonts/productSans.fnt"));

    private GuiText text;

    //private ParticleSystem system = new ParticleSystem(new TextureParticle(loader.loadTexture("particles/fire"), 8, false),50, 1,-0.1F, 3, 5);

    public LevelOverworld(EntityPlayer player) {
        super(player);
    }

    @Override
    public void initLevel() {
        lights.add(entitySun);
        entity = new Entity("dragon", "crate", new Vector3f(10, 0, -10), new Vector3f(0, 60, 0), 1, false);
        entityNm = new Entity("crate", "crate", new Vector3f(20, 20, -10), new Vector3f(0, 60, 0), 0.03F, true);

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
            entities.add(new Entity("pine", "pine", new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), new Vector3f(0, 0, 0), 1, false));
        }

        labyrinth.process();
        //entities.addAll(labyrinth.RenderLabyrinth());

        terrains.add(terrain);

        waters.add(new TileWater(renderer, this, 75, -75, 0));

        guiIngame = new GuiIngame(renderer);
        guiInventory = new GuiInventoryPlayer(renderer);
        guiMiniMap = new GuiMiniMap(renderer);
        guis.add(guiIngame);
        guis.add(guiMiniMap);

        currentGui.add(null);

        TextRenderingHelper.initTextRendering();

        text = new GuiText("Hello World! Hopefully...", 3, font, new FontEffect(), new Vector2f(0.5F, 0.5F), 0.5F, true);
    }

    @Override
    public void updateLevel() {
        glfwPollEvents();
        ParticleHelper.update(player);
        /*system.randomizeRotation();
        system.generateParticles(new Vector3f(20, 5, -20));*/
    }

    @Override
    public void renderLevel() {
        renderer.renderShadowMap(entities, entitiesNM, entitySun);
        entity.increaseRotation(0, 0.5F, 0);

        for (TileWater water : waters)
            water.renderWaterEffects();


        postProcessing.bindFrameBuffer();
        renderer.renderScene(entities, entitiesNM, terrains, lights, player, new Vector4f(0, -1, 0, 100000));
        renderer.getRendererWater().render(waters, lights, player);

        ParticleHelper.renderParticles(player);

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
            player.setCurrentGui(player.getCurrentGui() == null ? guiInventory : null);
            EverPhaseApi.ACHIEVEMENT_API.unlockAchievement(player, Achievements.testAch);
        }

        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F)) {
            player.toggleFlight();
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
        AudioHelper.clean();
    }
}
