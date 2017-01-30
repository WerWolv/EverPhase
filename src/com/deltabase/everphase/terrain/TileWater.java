package com.deltabase.everphase.terrain;

import com.deltabase.everphase.fbo.FrameBufferObject;
import com.deltabase.everphase.level.Level;
import com.deltabase.everphase.render.RendererMaster;
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
	private RendererMaster renderer;

	private FrameBufferObject fboReflection, fboRefraction;
	
	public TileWater(RendererMaster renderer, Level level, float centerX, float centerZ, float height){
		this.renderer = renderer;
		this.level = level;
		this.x = centerX;
		this.z = centerZ;
		this.height = height;

		fboReflection = renderer.getRendererWater().getFboReflection();
		fboRefraction = renderer.getRendererWater().getFboRefraction();
	}

	public void renderWaterEffects() {
		glEnable(GL30.GL_CLIP_DISTANCE0);

		for(TileWater water : level.getWaters()) {
			fboReflection.bindFrameBuffer();
			float distance = 2 * (level.getPlayer().getPosition().y - water.getHeight());
			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			renderer.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, 1, 0, -water.getHeight() + 1.0F));

			level.getPlayer().setPosition(new Vector3f(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y + distance, level.getPlayer().getPosition().z));
			level.getPlayer().setPitch(-level.getPlayer().getPitch());

			fboReflection.unbindFrameBuffer();
			fboRefraction.bindFrameBuffer();
			renderer.renderScene(level.getEntities(), level.getEntitiesNM(), level.getTerrains(), level.getLights(), level.getPlayer(), new Vector4f(0, -1, 0, water.getHeight() + 1.0F));
		}
		fboRefraction.unbindFrameBuffer();

		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
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
