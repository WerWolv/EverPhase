package com.werwolv.level;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.EntityParticle;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.gui.Gui;
import com.werwolv.gui.GuiInventory;
import com.werwolv.gui.GuiMinimap;
import com.werwolv.input.CursorListener;
import com.werwolv.input.KeyListener;
import com.werwolv.main.Main;
import com.werwolv.resource.TextureTerrainPack;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class LevelOverworld extends Level {

    private Entity entity, entityNm;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;

    //private Labyrinth labyrinth;

    private Gui guiMinimap, guiInventory;

    private EntityLight entitySun = new EntityLight(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1, 0.9F, 0.9F));

    private List<Gui> currentGui = new ArrayList<>();

    private boolean lastPressedE = false;

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

        // labyrinth = new Labyrinth(loader, 0, 0, 0);

        lights.add(entitySun);

        entities.add(entity);
        entitiesNM.add(entityNm);

        Random random = new Random();
        for (int i = 0; i < 512; i++) {
            int x = random.nextInt(250);
            int z = -random.nextInt(250);
            entities.add(new Entity(loader, "pine", "pine", new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), new Vector3f(0, 0, 0), 1, false));
        }

        //labyrinth.process();
        //entities.addAll(labyrinth.RenderLabyrinth());

        terrains.add(terrain);

        waters.add(new TileWater(renderer, this, 75, -75, 0));

        guiMinimap = new GuiMinimap(renderer, this, new Vector2f(0.85F, 0.75F), new Vector2f(0.30F, 0.30F));
        guiInventory = new GuiInventory(renderer, loader.loadTexture("barrel"), new Vector2f(0.85F, 0.5F), new Vector2f(0.3F, 0.3F));
        guis.add(guiMinimap);

        currentGui.add(null);

        ParticleManager.init(loader, renderer.getProjectionMatrix());
    }

    @Override
    public void updateLevel() {
        glfwPollEvents();
        handleInput();

        if(KeyListener.isKeyPressed(GLFW_KEY_Y)) new EntityParticle(new Vector3f(0, 1, 0), new Vector3f(0, 5, 0), 4, 4, 0, 1);

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
        renderer.getRendererGui().render(guis);

        if (player.getCurrentGui() != null) {
            renderer.getRendererGui().render(player.getCurrentGui());
        }
    }

    private void handleInput() {
        boolean currentE = KeyListener.isKeyPressed(GLFW_KEY_E);
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) System.exit(0);

        if (currentE && !lastPressedE) {
            if (player.getCurrentGui() != null) {
                player.setCurrentGui(null);
                Main.setCursorVisibility(false);
                CursorListener.enableCursorListener(true);
            } else {
                player.setCurrentGui(guiInventory);
                Main.setCursorVisibility(true);
                CursorListener.enableCursorListener(false);
            }
        }

        if (player.getCurrentGui() == null)
            player.move(terrain);

        lastPressedE = currentE;
    }

}
