package com.deltabase.everphase.world;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.engine.resource.TextureTerrainPack;
import com.deltabase.everphase.engine.toolbox.TextRenderingHelper;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.GuiIngame;
import com.deltabase.everphase.gui.inventory.GuiInventoryPlayer;
import com.deltabase.everphase.net.ConnectionHandlerUDP;
import com.deltabase.everphase.quest.QuestTest;
import com.deltabase.everphase.structure.Labyrinth;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.terrain.TileWater;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class WorldSurface extends World {

    private static int GUI_INVENTORY_ID;
    private Entity entity, entityNm;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;
    private Labyrinth labyrinth;
    private Gui guiIngame;
    private List<Gui> currentGui = new ArrayList<>();

    @Override
    public void initWorld() {
        this.spawnEntity(entitySun, new Vector3f(1000, 1000, -1000));
        entity = new Entity("dragon", "crate", new Vector3f(0, 60, 0), 1, new Vector3f(1.0F, 1.0F, 1.0F), false);
        entityNm = new Entity("crate", "crate", new Vector3f(0, 60, 0), 0.03F, new Vector3f(1.0F, 1.0F, 1.0F), true);
        entity.getModel().getTexture().setReflectivity(1.0F);
        entity.getModel().getTexture().setShineDamper(3);

        entityNm.getModel().getTexture().setReflectivity(0.5F);
        entityNm.getModel().getTexture().setShineDamper(10);
        entityNm.getModel().getTexture().setNormalMapID(EverPhaseApi.RESOURCE_LOADER.loadTexture("crateNormal"));
        entityNm.getModel().getTexture().setExtraInfoMapID(EverPhaseApi.RESOURCE_LOADER.loadTexture("crateSpecular"));

        textureTerrainPack = new TextureTerrainPack("grassy", "dirt", "path", "pinkFlowers", "blendMap");

        terrain = new Terrain(textureTerrainPack, "heightmap");
        this.addTerrain(terrain, 0, -1);

        //labyrinth = new Labyrinth(0, 0, 0, 3, 10);


        this.spawnEntity(entity, new Vector3f(10, 0, -10));
        this.spawnEntity(entityNm, new Vector3f(20, 20, -10));

        Random random = new Random();
        for (int i = 0; i < 127; i++) {
            int x = random.nextInt(250);
            int z = -random.nextInt(250);
            this.spawnEntity(new Entity("pine", "pine", new Vector3f(0, 0, 0), 1, new Vector3f(0.05F, 0.4F, 0.05F), false), new Vector3f(x, this.getCurrTerrain().getHeightOfTerrain(x, z), z));
        }

        //labyrinth.process();
        //this.spawnEntities(labyrinth.RenderLabyrinth());

        this.addWaterPlane(new TileWater(this), new Vector3f(75.0F, 0.0F, -75.0F));

        guiIngame = new GuiIngame(EverPhaseApi.getEverPhase().thePlayer);
        EverPhaseApi.GuiUtils.registerHUD(guiIngame);

        GUI_INVENTORY_ID = EverPhaseApi.GuiUtils.registerGui(new GuiInventoryPlayer());

        currentGui.add(null);

        TextRenderingHelper.initTextRendering();

        EverPhaseApi.QuestingApi.registerQuest("test", new QuestTest().setQuestDescription("Hello"));
        EverPhaseApi.QuestingApi.addQuestsToPlayer();

        EverPhaseApi.QuestingApi.startQuest("test");
    }

    @Override
    public void updateWorld() {
        super.updateWorld();

        for (Entity entity : getEntities())
            if (entity.getBoundingBox().intersectsWith(EverPhaseApi.getEverPhase().thePlayer.getBoundingBox()))
                ;//Do Something
    }

    @Override
    public void renderWorld() {
        super.renderWorld();
        entity.increaseRotation(0, 0.5F, 0);

    }

    @Override
    public void renderGUI() {
        super.renderGUI();
    }

    @Override
    public void handleInput() {
        super.handleInput();

        if (KeyCallback.isKeyPressedEdge(GLFW_KEY_E)) {
            EverPhaseApi.GuiUtils.displayGuiScreen(GUI_INVENTORY_ID);
        }

        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F)) {
            EverPhaseApi.getEverPhase().thePlayer.toggleFlight();
            EverPhaseApi.getEverPhase().thePlayer.HEALTH.setValue(EverPhaseApi.getEverPhase().thePlayer.HEALTH.getValue() - 10);
        }

        if (KeyCallback.isKeyPressed(GLFW_KEY_1))
            ConnectionHandlerUDP.sendMessageToServer("ADD:4+6");
    }

    public void clean() {
        super.clean();


    }
}
