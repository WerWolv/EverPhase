package com.deltabase.everphase.achievement;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.resource.TextureGui;

public class Achievement {

    private String name;
    private String description;
    private int achievementId = -1;
    private TextureGui texture;
    private boolean unlocked = false;

    public Achievement(String name, String description, String texturePath) {
        this.name = name;
        this.description = description;
        this.texture = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture(texturePath);

        EverPhaseApi.AchievementApi.addAchievement(this);
    }

    public void unlockAchievement() {
        this.unlocked = false;
    }

    public boolean isAchievementUnlocked() {
        return this.unlocked;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int id) {
        if (this.achievementId != -1)
            throw new IllegalAccessError("[EverPhase] The id of an achievement can only be set once");

        this.achievementId = id;
    }

    public TextureGui getTexture() {
        return texture;
    }
}
