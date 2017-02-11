package com.deltabase.everphase.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Quest {

    private String questDescription;

    private Queue<QuestTask> questTasks = new LinkedTransferQueue<>();
    private List<String> questTaskNames = new ArrayList<>();
    private Difficulty difficulty;
    private List<Quest> dependencies;

    public Quest(Difficulty difficulty, List<Quest> dependencies) {
        this.difficulty = difficulty;
        this.dependencies = dependencies;

        addQuestTasks();
    }

    public abstract void addQuestTasks();

    public QuestTask finishCurrentTask() {
        return questTasks.poll();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public Quest setQuestDescription(String questDescription) {
        this.questDescription = questDescription;

        return this;
    }

    public void addQuestTask(QuestTask questTask) {
        this.questTasks.add(questTask);
        this.questTaskNames.add(questTask.getTaskName());
    }

    public boolean doesQuestHasTask(String taskName) {
        return this.questTaskNames.contains(taskName);
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
