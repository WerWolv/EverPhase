package com.werwolv.level;

import com.werwolv.api.event.EventBus;
import com.werwolv.api.event.player.OpenGuiEvent;
import com.werwolv.callback.CursorPositionCallback;
import com.werwolv.callback.KeyCallback;
import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.EntityParticle;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.gui.Gui;
import com.werwolv.gui.GuiIngame;
import com.werwolv.gui.GuiInventory;
import com.werwolv.gui.GuiMinimap;
import com.werwolv.main.Main;
import com.werwolv.resource.TextureTerrainPack;
import com.werwolv.structure.Labyrinth;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class LevelOverworld extends Level {

    private Entity entity, entityNm;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;

    private Labyrinth labyrinth;

    private Gui guiMinimap, guiIngame, guiInventory;

    private EntityLight entitySun = new EntityLight(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1, 0.9F, 0.9F));

    private List<Gui> currentGui = new ArrayList<>();

    public LevelOverworld(EntityPlayer player) {
        super(player);
    }

    @Override
    public void initLevel() {
        entity = new Entity(loader, "dragon", "white", new Vector3f(10, 0, -10), new Vector3f(0, 60, 0), 1, false);
        entityNm = new Entity(loader, "crate", "crate", new Vector3f(20, 20, -10), new Vector3f(0, 60, 0), 0.03F, true);

        entityNm.getModel().getTexture().setReflectivity(0.5F);
        entityNm.getModel().getTexture().setShineDamper(10);
        entityNm.getModel().getTexture().setNormalMapID(loader.loadTexture("crateNormal"));

        textureTerrainPack = new TextureTerrainPack(loader,"grassy", "dirt", "path", "pinkFlowers", "blendMap");

        terrain = new Terrain(0, -1, loader, textureTerrainPack, "heightmap");

        labyrinth = new Labyrinth(loader, 0, 0, 0, 3, 10);

        lights.add(entitySun);

        /*entities.add(entity);
        entitiesNM.add(entityNm);

        Random random = new Random();
        for (int i = 0; i < 512; i++) {
            int x = random.nextInt(250);
            int z = -random.nextInt(250);
            entities.add(new Entity(loader, "pine", "pine", new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), new Vector3f(0, 0, 0), 1, false));
        }*/

        labyrinth.process();
        entities.addAll(labyrinth.RenderLabyrinth());

        terrains.add(terrain);

        waters.add(new TileWater(renderer, this, 75, -75, 0));

        guiMinimap = new GuiMinimap(renderer, this, new Vector2f(0.85F, 0.75F), new Vector2f(0.30F, 0.30F));
        guiIngame = new GuiIngame(renderer, 0, new Vector2f(0.85F, 0.5F), new Vector2f(0, 0));
        guiInventory = new GuiInventory(renderer, 0, new Vector2f(0, 0), new Vector2f(1, 1));

        guis.add(guiMinimap);
        guis.add(guiIngame);

        currentGui.add(null);

        ParticleManager.init(loader, renderer.getProjectionMatrix());
    }

    @Override
    public void updateLevel() {
        glfwPollEvents();
        handleInput();

        if (KeyCallback.isKeyPressed(GLFW_KEY_Y))
            new EntityParticle(new Vector3f(0, 1, 0), new Vector3f(0, 5, 0), 4, 4, 0, 1);

        ParticleManager.updateParticles();
    }

    @Override
    public void renderLevel() {
        renderer.renderShadowMap(entities, entitiesNM, entitySun);

        entity.increaseRotation(0, 0.5F, 0);

        for (TileWater water : waters)
           water.renderWaterEffects();

        renderer.renderScene(entities, entitiesNM, terrains, lights, player, new Vector4f(0, -1, 0, 100000));
        renderer.getRendererWater().render(waters, lights, player);

        ParticleManager.renderParticles(player);

    }

    @Override
    public void renderGUI() {

        for (Gui gui : guis)
            renderer.getRendererGui().render(gui);

        if (player.getCurrentGui() != null) {
            renderer.getRendererGui().render(player.getCurrentGui());
        }
    }

    private void handleInput() {
        if (KeyCallback.isKeyPressed(GLFW_KEY_ESCAPE)) System.exit(0);

        if (KeyCallback.isKeyPressedEdge(GLFW_KEY_E)) {
            if (player.getCurrentGui() != null) {
                player.setCurrentGui(null);
                Main.setCursorVisibility(false);
                CursorPositionCallback.enableCursorListener(true);
            } else {
                EventBus.postEvent(new OpenGuiEvent(player, guiInventory));
                Main.setCursorVisibility(true);
                CursorPositionCallback.enableCursorListener(false);
            }
        }

        if(KeyCallback.isKeyPressedEdge(GLFW_KEY_F))
            player.toggleFlight();

        if (player.getCurrentGui() == null)
            player.move(terrain);
    }

}
