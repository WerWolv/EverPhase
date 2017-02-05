package com.deltabase.everphase.engine.toolbox;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.font.FontType;
import com.deltabase.everphase.engine.font.TextMeshData;
import com.deltabase.everphase.engine.render.RendererFont;
import com.deltabase.everphase.gui.GuiText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextRenderingHelper {

    public static final Fonts FONTS = new Fonts();
    private static Map<FontType, List<GuiText>> texts = new HashMap<>();
    private static RendererFont renderer;

    public static void initTextRendering() {
        renderer = new RendererFont();
    }

    public static void loadText(GuiText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = EverPhaseApi.RESOURCE_LOADER.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());

    }

    public static void addText(GuiText text) {
        List<GuiText> textBatch = texts.computeIfAbsent(text.getFont(), k -> new ArrayList<>());

        textBatch.add(text);
    }

    public static void renderTexts() {
        renderer.render(texts);
    }

    public static void renderText(GuiText text) {
        renderer.renderText(text);
    }

    public static void removeText(GuiText text) {
        List<GuiText> textBatch = texts.get(text.getFont());
        if(textBatch == null) {
            return;
        }
        textBatch.remove(text);

        if(textBatch.isEmpty())
            texts.remove(text.getFont());
    }

    public static void clean() {
        renderer.clean();
    }

    public static class Fonts {
        public FontType fontProductSans = new FontType(EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("fonts/productSans").getTextureID(), new File("res/fonts/productSans.fnt"));
    }

}
