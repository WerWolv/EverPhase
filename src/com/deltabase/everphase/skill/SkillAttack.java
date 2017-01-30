package com.deltabase.everphase.skill;

public class SkillAttack extends Skill {


    public SkillAttack(String name, int textureID, int maxLevel) {
        super(name, textureID, 0, 0, maxLevel);
    }

    @Override
    public void onLevelUp() {
        System.out.println("Level up!");
    }
}
