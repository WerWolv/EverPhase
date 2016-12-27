package com.werwolv.shader;

import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

import java.util.List;

public class ShaderWater extends Shader {

	private int loc_modelMatrix;
	private int loc_viewMatrix;
	private int loc_projectionMatrix;
	private int loc_reflectionTexture, loc_refractionTexture, loc_dudvMap;
	private int loc_moveFactor;
	private int loc_cameraPos;
	private int loc_normalMap;
	private int loc_lightPos[], loc_lightColor[];

	public ShaderWater() {
		super("shaderWater", "shaderWater");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = getUniformLocation("projectionMatrix");
		loc_viewMatrix = getUniformLocation("viewMatrix");
		loc_modelMatrix = getUniformLocation("modelMatrix");

		loc_reflectionTexture = getUniformLocation("reflectionTexture");
		loc_refractionTexture = getUniformLocation("refractionTexture");
		loc_dudvMap = getUniformLocation("dudvMap");
		loc_normalMap = getUniformLocation("normalMap");

		loc_moveFactor = getUniformLocation("moveFactor");
		loc_cameraPos = getUniformLocation("cameraPos");

		loc_lightPos = new int[16];
		loc_lightColor = new int[16];

		for(int i = 0; i < 16; i++) {
			loc_lightPos[i] = getUniformLocation("lightPos[" + i + "]");
			loc_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
		}
	}

	public void connectsTextureUnits() {
		super.loadInteger(loc_reflectionTexture, 0);
		super.loadInteger(loc_refractionTexture, 1);
		super.loadInteger(loc_dudvMap, 2);
		super.loadInteger(loc_normalMap, 3);
	}

	public void loadLight(List<EntityLight> lights) {
		for(int i = 0; i < lights.size(); i++) {
			super.loadVector(loc_lightColor[i], lights.get(i).getColor());
			super.loadVector(loc_lightPos[i], lights.get(i).getPosition());
		}
	}

	public void loadMoveFactor(float moveFactor) {
		super.loadFloat(loc_moveFactor, moveFactor);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(loc_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);
		super.loadMatrix(loc_viewMatrix, viewMatrix);
		super.loadVector(loc_cameraPos, player.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix(loc_modelMatrix, modelMatrix);
	}

}
