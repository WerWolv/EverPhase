package com.werwolv.game.toolbox;

import com.werwolv.game.font.FontType;
import com.werwolv.game.gui.GuiText;
import com.werwolv.game.font.TextMeshData;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.render.RendererFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextRenderingHelper {

    private static ResourceLoader loader;
    private static Map<FontType, List<GuiText>> texts = new HashMap<>();
    private static RendererFont renderer;

    public static void initTextRendering(ResourceLoader loader) {
        renderer = new RendererFont();
        TextRenderingHelper.loader = loader;
    }

    public static void loadText(GuiText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GuiText> textBatch = texts.get(font);

        if(textBatch == null) {
            textBatch = new ArrayList<>();
            texts.put(font, textBatch);
        }

        textBatch.add(text);
    }

    public static void renderTexts() {
        renderer.render(texts);
    }

    public static void removeText(GuiText text) {
        List<GuiText> textBatch = texts.get(text.getFont());
        if(textBatch == null) {
            System.err.println("Text is already removed!");
            return;
        }
        textBatch.remove(text);

        if(textBatch.isEmpty())
            texts.remove(text.getFont());
    }

    public static void clean() {
        renderer.clean();
    }

}
