package com.deltabase.everphase.quest;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Quest {

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
        questTasks.poll();

        if (questTasks.isEmpty()) {

        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
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
