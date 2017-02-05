package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.engine.font.FontType;
import com.deltabase.everphase.engine.shader.ShaderFont;
import com.deltabase.everphase.gui.GuiText;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class RendererFont {

	private ShaderFont shader;

	public RendererFont() {
		shader = new ShaderFont();
	}

	public void clean(){
		shader.clean();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		shader.start();
	}

	public void render(Map<FontType, List<GuiText>> texts) {
		prepare();

		for(FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());

			for(GuiText text : texts.get(font))
				renderWithoutBindText(text);
		}

		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

    private void renderWithoutBindText(GuiText text) {
        GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		shader.loadColor(text.getColor());
		shader.loadTranslation(new Vector2f((text.getPosition().x() + 1) / 2, (text.getPosition().y() + 1) / 2));
		shader.loadFontEffect(text.getFontEffect());

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCnt());

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	public void renderText(GuiText text) {
		shader.start();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, text.getFont().getTextureAtlas());
		renderWithoutBindText(text);
		shader.stop();
	}

}
