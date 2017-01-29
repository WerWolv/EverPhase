package com.werwolv.game.shader;


import org.joml.Vector2f;
import org.joml.Vector3f;

public class ShaderFont extends Shader {

	private int loc_color, loc_translation;

	public ShaderFont() {
		super("shaderFont", "shaderFont");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_color = super.getUniformLocation("color");
		loc_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	public void loadColor(Vector3f color) {
		super.loadVector(loc_color, color);
	}

	public void loadTranslation(Vector2f translation) {
		super.loadVector(loc_translation, translation);
	}


}
