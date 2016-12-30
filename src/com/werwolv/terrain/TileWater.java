package com.werwolv.terrain;

import com.werwolv.fbo.FrameBufferWater;
import com.werwolv.level.Level;
import com.werwolv.render.RendererMaster;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.glEnable;

public class TileWater {
	
	public static final float TILE_SIZE = 60;
	
	private float height;
	private float x,z;

	private Level level;
	private FrameBufferWater fboWater;
	private RendererMaster renderer;
	
	public TileWater(RendererMaster renderer, Level level, float centerX, float centerZ, float height){
		this.renderer = renderer;
		this.level = level;
		this.x = centerX;
		this.z = centerZ;
		this.height = height;

		fboWater = renderer.getFboWater();
	}

	public void renderWaterEffects() {
		glEnable(GL30.GL_CLIP_DISTANCE0);

		for(TileWater water : level.getWaters()) {
			fboWater.bindReflectionFrameBuffer();
			float distance = 2 * (level.getPlayer().getPosition().y - water.getHeight());
			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			renderer.renderScene(level.getEntities(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, 1, 0, -water.getHeight() + 1.0F));

			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			fboWater.bindRefractionFrameBuffer();
			renderer.renderScene(level.getEntities(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, -1, 0, water.getHeight() + 1.0F));
		}

		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

		fboWater.unbindCurrentFrameBuffer();

	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
