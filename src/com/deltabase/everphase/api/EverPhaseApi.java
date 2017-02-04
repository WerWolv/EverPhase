package com.deltabase.everphase.api;

import com.deltabase.everphase.achievement.Achievement;
import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.advance.AchievementGetEvent;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Main;

import java.util.ArrayList;
import java.util.List;

public class EverPhaseApi {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    public static final AchievementApi ACHIEVEMENT_API = new AchievementApi();

    public static class AchievementApi {

        private static final double DISPLAY_TIME = 5.0D;

        private List<Achievement> listAchievement = new ArrayList<>();
        private Achievement currProcessedAchievement;

        private double achievementDisplayTicker = 0;

        public void addAchievement(Achievement achievement) {
            achievement.setAchievementId(listAchievement.size());
            listAchievement.add(achievement);
        }

        public void unlockAchievement(EntityPlayer player, Achievement achievement) {
            for (Achievement ach : listAchievement) {
                if (ach.getAchievementId() == achievement.getAchievementId()) {
                    if (!ach.isAchievementUnlocked()) {
                        EVENT_BUS.postEvent(new AchievementGetEvent(player, achievement));
                        ach.unlockAchievement();
                        listAchievement.set(listAchievement.indexOf(ach), ach);
                        achievementDisplayTicker = DISPLAY_TIME;
                        currProcessedAchievement = achievement;
                    }
                }
            }
        }

        public boolean hasAchievementBeenUnlocked(Achievement achievement) {
            for (Achievement ach : listAchievement)
                if (ach.getAchievementId() == achievement.getAchievementId())
                    return ach.isAchievementUnlocked();

            return false;
        }

        public boolean isAchievementBeingDisplayed() {
            if (achievementDisplayTicker > 0) {
                achievementDisplayTicker -= Main.getFrameTimeSeconds();
                return true;
            }

            currProcessedAchievement = null;
            return false;
        }

        public double getAchievementPlayProgress() {
            return (achievementDisplayTicker / DISPLAY_TIME) * 100;
        }

        public Achievement getCurrentlyProcessedAchievement() {
            return currProcessedAchievement;
        }
    }
}
