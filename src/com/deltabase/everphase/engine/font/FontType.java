package com.deltabase.everphase.engine.font;

import com.deltabase.everphase.gui.GuiText;

import java.io.File;

/**
 * Represents a fontProductSans. It holds the fontProductSans's texture atlas as well as having the
 * ability to create the quad vertices for any text using this fontProductSans.
 */
public class FontType {

	private int textureAtlas;
    private TextMeshCreator meshCreator;

	/**
     * Creates a new fontProductSans and loads up the data about each character from the
     * fontProductSans file.
     *
	 * @param textureAtlas
     *            - the ID of the fontProductSans atlas texture.
     * @param fontFile
     *            - the fontProductSans file containing information about each character in
     *            the texture atlas.
	 */
	public FontType(int textureAtlas, File fontFile) {
		this.textureAtlas = textureAtlas;
        this.meshCreator = new TextMeshCreator(fontFile);
    }

	/**
     * @return The fontProductSans texture atlas.
     */
	public int getTextureAtlas() {
		return textureAtlas;
	}

	/**
	 * Takes in an unloaded text and calculate all of the vertices for the quads
	 * on which this text will be rendered. The vertex positions and texture
     * coords and calculated based on the information from the fontProductSans file.
     *
	 * @param text
	 *            - the unloaded text.
	 * @return Information about the vertices of all the quads.
	 */
	public TextMeshData loadText(GuiText text) {
        return meshCreator.createTextMesh(text);
    }

}
