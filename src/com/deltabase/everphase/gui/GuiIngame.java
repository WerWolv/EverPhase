package com.deltabase.everphase.gui;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.font.FontType;
import com.deltabase.everphase.engine.render.RendererGui;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.main.Main;
import org.joml.Vector4f;

import java.io.File;

public class GuiIngame extends Gui {

    private TextureGui textureGuiIngame;
    private float achievementDialogYPos = 0.0F;
    private FontType font = new FontType(EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("fonts/productSans").getTextureID(), new File("res/fonts/productSans.fnt"));

    private EntityPlayer player;

    public GuiIngame(EntityPlayer player) {
        this.player = player;
        textureGuiIngame = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("gui/guiIngame");
    }

    @Override
    public void render(RendererGui renderer) {
        renderer.drawTexture(0, -1.8F, 1.0F, new Vector4f(0, 0, 256, 24), textureGuiIngame);

        renderer.drawTexture(-1.563F / Main.getAspectRatio() + 0.1955F * player.getSelectedItemIndex() / Main.getAspectRatio(), -1.5975F, 1.0F, new Vector4f(215, 25, 256, 50), textureGuiIngame);

        renderer.setDrawColor(new Vector4f(1.0F, 0.5F, 0.5F, 0.0F));
        renderer.drawTexture(-0.05F, -1.355F, 1.0F, new Vector4f(30, 45, 30 + Math.max((64.0F / player.HEALTH.getMaxValue()) * player.HEALTH.getValue(), 0), 53), textureGuiIngame);
        renderer.drawTexture(-0.05F, -1.45F, 1.0F, new Vector4f(25, 31, 99, 43), textureGuiIngame);

        if (EverPhaseApi.AchievementApi.isAchievementBeingDisplayed()) {
            if (EverPhaseApi.AchievementApi.getAchievementPlayProgress() < 11)
                achievementDialogYPos += 0.01F;
            if (EverPhaseApi.AchievementApi.getAchievementPlayProgress() > 89)
                achievementDialogYPos -= 0.01F;
            renderer.drawTexture(0.5F, 0.55F + achievementDialogYPos, 1.0F, new Vector4f(115, 29, 211, 66), textureGuiIngame);
            renderer.drawTexture(0.51F, 1.185F + achievementDialogYPos, Slot.SLOT_SIZE, new Vector4f(0, 0, EverPhaseApi.AchievementApi.getCurrentlyProcessedAchievement().getTexture().getSize(), EverPhaseApi.AchievementApi.getCurrentlyProcessedAchievement().getTexture().getSize()), EverPhaseApi.AchievementApi.getCurrentlyProcessedAchievement().getTexture());
        }
    }
}
