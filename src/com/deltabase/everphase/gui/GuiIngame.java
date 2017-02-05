package com.deltabase.everphase.gui;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.font.FontType;
import com.deltabase.everphase.engine.font.effects.FontEffect;
import com.deltabase.everphase.engine.render.RendererMaster;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.main.Main;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;

public class GuiIngame extends Gui {

    private TextureGui textureGuiIngame;
    private float achievementDialogYPos = 0.0F;
    private FontType font = new FontType(EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("fonts/productSans").getTextureID(), new File("res/fonts/productSans.fnt"));
    private GuiText text = new GuiText("", 3, font, new FontEffect(), new Vector2f(0.51F, 1.185F + achievementDialogYPos), 1.0F, false);

    public GuiIngame(RendererMaster renderer) {
        super(renderer);

        textureGuiIngame = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("gui/guiIngame");
    }

    @Override
    public void render() {
        renderer.getRendererGui().drawTexture(0, -1.8F, 1.0F, new Vector4f(0, 0, 256, 24), textureGuiIngame);

        renderer.getRendererGui().drawTexture(-1.563F / Main.getAspectRatio() + 0.1955F * Main.getPlayer().getSelectedItem() / Main.getAspectRatio(), -1.5975F, 1.0F, new Vector4f(215, 25, 256, 50), textureGuiIngame);

        if (EverPhaseApi.ACHIEVEMENT_API.isAchievementBeingDisplayed()) {
            text.remove();
            if (EverPhaseApi.ACHIEVEMENT_API.getAchievementPlayProgress() < 11)
                achievementDialogYPos += 0.01F;
            if (EverPhaseApi.ACHIEVEMENT_API.getAchievementPlayProgress() > 89)
                achievementDialogYPos -= 0.01F;
            renderer.getRendererGui().drawTexture(0.5F, 0.55F + achievementDialogYPos, 1.0F, new Vector4f(115, 29, 211, 66), textureGuiIngame);
            renderer.getRendererGui().drawTexture(0.51F, 1.185F + achievementDialogYPos, Slot.SLOT_SIZE, new Vector4f(0, 0, EverPhaseApi.ACHIEVEMENT_API.getCurrentlyProcessedAchievement().getTexture().getSize(), EverPhaseApi.ACHIEVEMENT_API.getCurrentlyProcessedAchievement().getTexture().getSize()), EverPhaseApi.ACHIEVEMENT_API.getCurrentlyProcessedAchievement().getTexture());
            renderer.getRendererGui().drawString("Hello World", 0.0F, 0.0F, 1.0F);
        }



        /*renderer.getRendererGui().drawTexture(-0.5F, 0.2F, 0.5F, new Vector4f(0, 195, 256, 122), textureGuiIngame);

        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 188, 45, 103), textureGuiIngame);
        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 175, 40, 100), textureGuiIngame);
        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 163, 35, 95), textureGuiIngame);*/

    }
}
