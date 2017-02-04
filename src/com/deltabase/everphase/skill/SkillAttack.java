package com.deltabase.everphase.skill;

public class SkillAttack extends Skill {


    public SkillAttack(String name, String texturePath, int maxLevel) {
        super(name, texturePath, 0, 0, maxLevel);
    }

    @Override
    public void onLevelUp() {
    }
}
