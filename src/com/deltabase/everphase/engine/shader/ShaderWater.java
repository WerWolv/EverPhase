package com.deltabase.everphase.engine.shader;

import com.deltabase.everphase.engine.toolbox.Maths;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.entity.EntityPlayer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class ShaderWater extends Shader {

	private int loc_modelMatrix;
	private int loc_viewMatrix;
	private int loc_projectionMatrix;
	private int loc_reflectionTexture, loc_refractionTexture, loc_dudvMap;
	private int loc_moveFactor;
	private int loc_cameraPos;
	private int loc_normalMap;
	private int loc_lightPos[], loc_lightColor[], loc_attenuation[];
	private int loc_depthMap;
	private int loc_nearPlane, loc_farPlane;

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

		loc_depthMap = getUniformLocation("depthMap");

		loc_nearPlane = getUniformLocation("nearPlane");
		loc_farPlane = getUniformLocation("farPlane");

		loc_lightPos = new int[16];
		loc_lightColor = new int[16];
		loc_attenuation = new int[16];

		for(int i = 0; i < 16; i++) {
			loc_lightPos[i] = getUniformLocation("lightPos[" + i + "]");
			loc_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
			loc_attenuation[i] = getUniformLocation("attenuation[" + i + "]");
		}
	}

	public void connectsTextureUnits() {
		super.loadInteger(loc_reflectionTexture, 0);
		super.loadInteger(loc_refractionTexture, 1);
		super.loadInteger(loc_dudvMap, 2);
		super.loadInteger(loc_normalMap, 3);
		super.loadInteger(loc_depthMap, 4);
	}

	public void loadLight(List<EntityLight> lights) {
		for(int i = 0; i < 16; i++) {
			if(i < lights.size()) {
				super.loadVector(loc_lightPos[i], lights.get(i).getPosition());
				super.loadVector(loc_lightColor[i], lights.get(i).getColor());
				super.loadVector(loc_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(loc_lightPos[i], new Vector3f(0.0F, 0.0F, 0.0F));
				super.loadVector(loc_lightColor[i], new Vector3f(0.0F, 0.0F, 0.0F));
				super.loadVector(loc_attenuation[i], new Vector3f(0.0F, 0.0F, 0.0F));
			}
		}
	}

	public void loadNearAndFarPlane(float near, float far) {
		super.loadFloat(loc_nearPlane, near);
		super.loadFloat(loc_farPlane, far);
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
