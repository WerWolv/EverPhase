package com.werwolv.game.skill;

public abstract class Skill {

    private String name;
    private int textureID;
    private int skillID;
    private int maxLevel = 99;
    private int currLevel;
    private int currExperience;
    private int xOffset, yOffset;

    protected Skill(String name, int textureID, int xOffset, int yOffset, int skillID) {
        this.name = name;
        this.textureID = textureID;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.skillID = skillID;
    }

    public void levelUp() {
        if (currLevel < maxLevel)
            currLevel++;
    }

    public abstract void onLevelUp();

    public int getCurrentXP() {
        return currExperience;
    }

    public int getXPToNextLevel(int level) {
        return getXPForLevel(level) - currExperience;
    }

    public int getXPForLevel(int level) {
        int points = 0;
        double output = 0;

        for (int lvl = 1; lvl <= maxLevel; lvl++)
        {
            points += Math.floor(lvl + 300 * Math.pow(2, lvl / 7.));
            if (lvl == level)
                return (int) output;
            output = Math.floor(points / 4);
        }

        return -1;
    }

    public String getName() {
        return name;
    }

    public int[] getTextureOffset() {
        return new int[]{xOffset, yOffset};
    }

    public int getCurrentLevel() {
        return currLevel;
    }
}
