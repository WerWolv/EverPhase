package com.deltabase.everphase.api;

import com.deltabase.everphase.achievement.Achievement;
import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.advance.AchievementGetEvent;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.entity.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class EverPhaseApi {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    private static List<Achievement> listAchievement = new ArrayList<>();

    public static void addAchievement(Achievement achievement) {
        achievement.setAchievementId(listAchievement.size());
        listAchievement.add(achievement);
    }

    public static void unlockAchievement(EntityPlayer player, Achievement achievement) {
        for (Achievement ach : listAchievement) {
            if (ach.getAchievementId() == achievement.getAchievementId()) {
                if (!ach.isAchievementUnlocked()) {
                    EVENT_BUS.postEvent(new AchievementGetEvent(player, achievement));
                    ach.unlockAchievement();
                    listAchievement.set(listAchievement.indexOf(ach), ach);
                }
            }
        }
    }

    public static boolean hasAchievementBeenUnlocked(Achievement achievement) {
        for (Achievement ach : listAchievement)
            if (ach.getAchievementId() == achievement.getAchievementId())
                return ach.isAchievementUnlocked();

        return false;
    }

}
