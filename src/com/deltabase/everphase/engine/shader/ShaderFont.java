package com.deltabase.everphase.engine.shader;


import com.deltabase.everphase.engine.font.effects.FontEffect;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ShaderFont extends Shader {

	private int loc_color, loc_translation;
	private int loc_width, loc_borderWidth;
	private int loc_edge, loc_borderEdge;
	private int loc_outlineColor;
	private int loc_offset;

	public ShaderFont() {
		super("shaderFont", "shaderFont");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_color = super.getUniformLocation("color");
		loc_translation = super.getUniformLocation("translation");

		loc_width = super.getUniformLocation("width");
		loc_borderEdge = super.getUniformLocation("borderWidth");
		loc_edge = super.getUniformLocation("edge");
		loc_borderEdge = super.getUniformLocation("borderEdge");
		loc_outlineColor = super.getUniformLocation("outlineColor");
		loc_offset = super.getUniformLocation("offset");
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

	public void loadFontEffect(FontEffect effect) {
		super.loadFloat(loc_width, effect.getCharWidth());
		super.loadFloat(loc_edge, effect.getCharEdgeWidth());
		super.loadFloat(loc_borderWidth, effect.getCharBorderWidth());
		super.loadFloat(loc_borderEdge, effect.getCharBorderEdgeWidth());
		super.loadVector(loc_offset, effect.getOffset());
		super.loadVector(loc_color, effect.getColor());
	}

}
