package com.werwolv.gui;

import com.werwolv.fbo.FrameBufferMinimap;
import com.werwolv.level.Level;
import com.werwolv.render.RendererMaster;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GuiMinimap extends Gui{

    private FrameBufferMinimap fboMiniMap = new FrameBufferMinimap();

    private Level level;

    public GuiMinimap(RendererMaster renderer, Level level, Vector2f position, Vector2f scale) {
        super(renderer, 0, position, scale);

        this.setTexture(fboMiniMap.getMiniMapTexture());

        this.level = level;
    }


    public void render() {
        fboMiniMap.bindMinimapFrameBuffer();

        float lastPitch = level.getPlayer().getPitch();
        Vector3f playerPos = level.getPlayer().getPosition();
        level.getPlayer().setPitch(90.0F);
        level.getPlayer().setPosition(new Vector3f(playerPos.x, level.getCurrTerrain().getHeightOfTerrain(playerPos.x, playerPos.z) + 100.0F, playerPos.z));
        renderer.getRendererWater().renderWithoutEffects(level.getWaters());
        renderer.renderScene(level.getEntities(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, -1, 0, 1000));
        level.getPlayer().setPosition(playerPos);
        level.getPlayer().setPitch(lastPitch);

        fboMiniMap.unbindCurrentFrameBuffer();
    }

}
