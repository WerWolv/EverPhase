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
			float distance = 2 * (level.getPlayer().getPosition().y - water.getY());
			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, 1, 0, -water.getY() + 1.0F));

			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			fboReflection.unbindFrameBuffer();
			fboRefraction.bindFrameBuffer();
			EverPhaseApi.RendererUtils.RENDERER_MASTER.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, -1, 0, water.getY() + 1.0F));
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
