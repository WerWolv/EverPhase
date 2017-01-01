package com.werwolv.level;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.EntityParticle;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.gui.Gui;
import com.werwolv.gui.GuiMinimap;
import com.werwolv.input.KeyListener;
import com.werwolv.resource.TextureTerrainPack;
import com.werwolv.structure.Labyrinth;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class LevelOverworld extends Level {

    private Entity entity, entityNm;
    private TextureTerrainPack textureTerrainPack;
    private Terrain terrain;

    private Labyrinth labyrinth;

    private Gui minmapGui;

    public LevelOverworld(EntityPlayer player) {
        super(player);
    }

    @Override
    public void initLevel() {
        entity = new Entity(loader, "dragon", "white", new Vector3f(0, 0, -10), new Vector3f(0, 60, 0), 1, false);
        entityNm = new Entity(loader, "crate", "crate", new Vector3f(20, 20, -10), new Vector3f(0, 60, 0), 0.03F, true);

        entityNm.getModel().getTexture().setReflectivity(0.5F);
        entityNm.getModel().getTexture().setShineDamper(10);
        entityNm.getModel().getTexture().setNormalMapID(loader.loadTexture("crateNormal"));

        textureTerrainPack = new TextureTerrainPack(loader,"grassy", "dirt", "path", "pinkFlowers", "blendMap");

        terrain = new Terrain(0, -1, loader, textureTerrainPack, "heightmap");

        labyrinth = new Labyrinth(loader, 0, 0, 0);

        lights.add(new EntityLight(new Vector3f(-100, 100, -100), new Vector3f(1, 0.9F, 0.9F)));
        lights.add(new EntityLight(new Vector3f(-50, 10, -100), new Vector3f(0, 1, 0), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(-20, 10, -75), new Vector3f(0, 0, 1), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(50, 10, -100), new Vector3f(1, 1, 0), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(30, 10, 30), new Vector3f(1, 0, 1), new Vector3f(1, 0.01F, 0.002F)));

        entities.add(entity);
        entitiesNM.add(entityNm);

        labyrinth.process();
        entities.addAll(labyrinth.RenderLabyrinth());

        terrains.add(terrain);

        waters.add(new TileWater(renderer, this, 75, -75, 0));

        minmapGui = new GuiMinimap(renderer, this, new Vector2f(0.85F, 0.75F), new Vector2f(0.30F, 0.30F));
        guis.add(minmapGui);
        ParticleManager.init(loader, renderer.getProjectionMatrix());
    }

    @Override
    public void updateLevel() {
        glfwPollEvents();
        handleInput();
        player.move(terrain);

        if(KeyListener.isKeyPressed(GLFW_KEY_Y)) new EntityParticle(new Vector3f(0, 1, 0), new Vector3f(0, 5, 0), 4, 4, 0, 1);

        ParticleManager.updateParticles();
    }

    @Override
    public void renderLevel() {
       entity.increaseRotation(0, 0.5F, 0);

       for(TileWater water : waters)
           water.renderWaterEffects();

        renderer.renderScene(entities, entitiesNM, terrains, lights, player, new Vector4f(0, -1, 0, 100000));
        renderer.getRendererWater().render(waters, lights, player);

        ParticleManager.renderParticles(player);

    }

    @Override
    public void renderGUI() {
        renderer.getRendererGui().render(guis);
    }

    private void handleInput() {
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) System.exit(0);
    }

}
