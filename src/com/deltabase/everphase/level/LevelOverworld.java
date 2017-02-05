package com.deltabase.everphase.level;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.engine.audio.AudioHelper;
import com.deltabase.everphase.engine.fbo.FrameBufferObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class LevelOverworld extends Level {

    private Entity entity, entityNm, entityPine;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;

    private Labyrinth labyrinth;

    private Gui guiIngame, guiInventory, guiMiniMap;

    private EntityLight entitySun = new EntityLight(new Vector3f(1, 0.9F, 0.9F), new Vector3f(1, 0, 0));

    private List<Gui> currentGui = new ArrayList<>();

    private FrameBufferObject postProcessing = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1]);
    private FrameBufferObject outputFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);
    private FrameBufferObject miniMapFBO = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);

    private GuiText text;

    //private ParticleSystem system = new ParticleSystem(new TextureParticle(EverPhaseApi.RESOURCE_LOADER.loadTexture("particles/fire"), 8, false),50, 1,-0.1F, 3, 5);

    public LevelOverworld(EntityPlayer player) {
        super(player);
    }

    @Override
    public void initLevel() {
        this.spawnEntity(entitySun, new Vector3f(1000, 1000, -1000));
        entity = new Entity("dragon", "crate", new Vector3f(0, 60, 0), 1, false);
        entityNm = new Entity("crate", "crate", new Vector3f(0, 60, 0), 0.03F, true);
        entity.getModel().getTexture().setReflectivity(1.0F);
        entity.getModel().getTexture().setShineDamper(3);

        entityNm.getModel().getTexture().setReflectivity(0.5F);
        entityNm.getModel().getTexture().setShineDamper(10);
        entityNm.getModel().getTexture().setNormalMapID(EverPhaseApi.RESOURCE_LOADER.loadTexture("crateNormal"));
        entityNm.getModel().getTexture().setExtraInfoMapID(EverPhaseApi.RESOURCE_LOADER.loadTexture("crateSpecular"));

        textureTerrainPack = new TextureTerrainPack("grassy", "dirt", "path", "pinkFlowers", "blendMap");

        terrain = new Terrain(textureTerrainPack, "heightmap");
        this.addTerrain(terrain, 0, -1);

        labyrinth = new Labyrinth(0, 0, 0, 3, 10);


        this.spawnEntity(entity, new Vector3f(10, 0, -10));
        this.spawnEntity(entityNm, new Vector3f(20, 20, -10));

        Random random = new Random();
        for (int i = 0; i < 127; i++) {
            int x = random.nextInt(250);
            int z = -random.nextInt(250);
            this.spawnEntity(new Entity("pine", "pine", new Vector3f(0, 0, 0), 1, false), new Vector3f(x, this.getCurrTerrain().getHeightOfTerrain(x, z), z));
        }

        labyrinth.process();
        //entities.addAll(labyrinth.RenderLabyrinth());

        this.addWaterPlane(new TileWater(this), new Vector3f(75.0F, 0.0F, -75.0F));

        guiIngame = new GuiIngame();
        guiInventory = new GuiInventoryPlayer();
        guiMiniMap = new GuiMiniMap();
        EverPhaseApi.GuiUtils.registerGui(guiIngame);

        currentGui.add(null);

        TextRenderingHelper.initTextRendering();

        text = new GuiText("Hello World! Hopefully...", 3, TextRenderingHelper.FONTS.fontProductSans, new FontEffect(), new Vector2f(0.5F, 0.5F), 0.5F, true);

        player.setPosition(getEntities().get(0).getPosition());
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
        EverPhaseApi.RendererUtils.RENDERER_MASTER.renderShadowMap(this.getEntities(), this.getEntitiesNM(), entitySun);
        entity.increaseRotation(0, 0.5F, 0);

        for (TileWater water : this.getWaters())
            water.renderWaterEffects();


        postProcessing.bindFrameBuffer();
        EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(this.getEntities(), this.getEntitiesNM(), this.getTerrains(), this.getLights(), player, new Vector4f(0, -1, 0, 100000));
        EverPhaseApi.RendererUtils.RENDERER_WATER.render(this.getWaters(), this.getLights(), player);

        ParticleHelper.renderParticles(player);

        postProcessing.unbindFrameBuffer();

        postProcessing.resolveToFBO(outputFBO);
        PostProcessing.doPostProcessing(outputFBO.getColorTexture());
    }

    @Override
    public void renderGUI() {

        for (Gui gui : EverPhaseApi.GuiUtils.getRegisteredGuis())
            EverPhaseApi.RendererUtils.RENDERER_GUI.render(gui);

        if (player.getCurrentGui() != null) {
            EverPhaseApi.RendererUtils.RENDERER_GUI.render(player.getCurrentGui());
        }

        TextRenderingHelper.renderTexts();

    }

    @Override
    public void handleInput() {
        super.handleInput();

        if (KeyCallback.isKeyPressedEdge(GLFW_KEY_E)) {
            player.setCurrentGui(player.getCurrentGui() == null ? guiInventory : null);
        }

        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F)) {
            player.toggleFlight();
        }

        if (player.getCurrentGui() == null) {
            player.onMove(getCurrTerrain());
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
