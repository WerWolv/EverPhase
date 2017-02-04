package com.deltabase.everphase.api.event.advance;

import com.deltabase.everphase.achievement.Achievement;
import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityPlayer;

public class AchievementGetEvent extends Event {

    private EntityPlayer player;
    private Achievement achievement;


    public AchievementGetEvent(EntityPlayer player, Achievement achievement) {
        super("ACHIEVEMENTGETEVENT");

        this.player = player;
        this.achievement = achievement;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Achievement getAchievement() {
        return achievement;
    }
}
