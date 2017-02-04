package com.deltabase.everphase.achievement;

import com.deltabase.everphase.api.EverPhaseApi;

public class Achievement {

    private String name;
    private String description;
    private int achievementId = -1;
    private int textureId;
    private boolean unlocked = false;

    public Achievement(String name, String description, String texturePath) {
        this.name = name;
        this.description = description;
        this.textureId = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture(texturePath).getTextureID();
    }

    public void unlockAchievement() {
        this.unlocked = true;
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

    public int getTextureId() {
        return textureId;
    }
}
