package com.deltabase.everphase.quest;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.event.quest.QuestFinishedEvent;
import com.deltabase.everphase.api.event.quest.QuestTaskFinishedEvent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Quest {

    private String questName, questDescription;

    private Queue<QuestTask> questTasks = new LinkedTransferQueue<>();
    private Difficulty difficulty;
    private List<Quest> dependencies;

    public Quest(Difficulty difficulty, List<Quest> dependencies) {
        this.difficulty = difficulty;
        this.dependencies = dependencies;

        addQuestTasks();
    }

    public abstract void addQuestTasks();

    public void finishCurrentTask() {

        if (questTasks.isEmpty()) {
            EverPhaseApi.EVENT_BUS.postEvent(new QuestFinishedEvent(this));
            EverPhaseApi.QuestingApi.finishQuest(this.getQuestName());
            return;
        }

        EverPhaseApi.EVENT_BUS.postEvent(new QuestTaskFinishedEvent(this, questTasks.poll()));
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getQuestName() {
        return questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public QuestTask getCurrentTask() {
        return questTasks.peek();
    }

    public List<Quest> getDependencies() {
        return dependencies;
    }

    public Queue<QuestTask> getLeftOverQuestTasks() {
        return questTasks;
    }

    public enum Difficulty {
        NOVICE,
        INTERMEDIATE,
        EXPERIENCED,
        MASTER,
        GRANDMASTER,
        SPECIAL
    }
}
