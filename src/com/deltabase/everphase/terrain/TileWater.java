package com.deltabase.everphase.terrain;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.fbo.FrameBufferObject;
import com.deltabase.everphase.level.Level;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.glEnable;

public class TileWater {
	
	public static final float TILE_SIZE = 60;

	private float x, y, z;

	private Level level;

	private FrameBufferObject fboReflection, fboRefraction;

	public TileWater(Level level) {
		this.level = level;

		fboReflection = EverPhaseApi.RendererUtils.RENDERER_WATER.getFboReflection();
		fboRefraction = EverPhaseApi.RendererUtils.RENDERER_WATER.getFboRefraction();
	}

	public void renderWaterEffects() {
		glEnable(GL30.GL_CLIP_DISTANCE0);

		for(TileWater water : level.getWaters()) {
			fboReflection.bindFrameBuffer();
            float distance = 2 * (EverPhaseApi.getEverPhase().thePlayer.getPosition().y - water.getY());
            EverPhaseApi.getEverPhase().thePlayer.setPosition(new Vector3f(EverPhaseApi.getEverPhase().thePlayer.getPosition().x, EverPhaseApi.getEverPhase().thePlayer.getPosition().y - distance, EverPhaseApi.getEverPhase().thePlayer.getPosition().z));
            EverPhaseApi.getEverPhase().thePlayer.setPitch(-EverPhaseApi.getEverPhase().thePlayer.getPitch());

            EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), EverPhaseApi.getEverPhase().thePlayer, new Vector4f(0, 1, 0, -water.getY() + 1.0F));

            EverPhaseApi.getEverPhase().thePlayer.setPosition(new Vector3f(EverPhaseApi.getEverPhase().thePlayer.getPosition().x, EverPhaseApi.getEverPhase().thePlayer.getPosition().y + distance, EverPhaseApi.getEverPhase().thePlayer.getPosition().z));
            EverPhaseApi.getEverPhase().thePlayer.setPitch(-EverPhaseApi.getEverPhase().thePlayer.getPitch());

			fboReflection.unbindFrameBuffer();
			fboRefraction.bindFrameBuffer();
            EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), EverPhaseApi.getEverPhase().thePlayer, new Vector4f(0, -1, 0, water.getY() + 1.0F));
        }
		fboRefraction.unbindFrameBuffer();

		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
	}

	public void setCenterPosition(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.y = height;
		this.z = centerZ;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}



}
