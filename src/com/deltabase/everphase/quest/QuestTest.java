package com.deltabase.everphase.quest;

import org.joml.Vector3f;

import java.util.ArrayList;

public class QuestTest extends Quest {


    public QuestTest() {
        super("test", Difficulty.GRANDMASTER, new ArrayList<>());
    }

    @Override
    public void addQuestTasks() {
        addQuestTask(new QuestTask("test0", "asdasdasd0", new Vector3f(0.0F, 0.0F, 0.0F)));
        addQuestTask(new QuestTask("test1", "asdasdasd1", new Vector3f(0.0F, 0.0F, 0.0F)));
        addQuestTask(new QuestTask("test2", "asdasdasd2", new Vector3f(0.0F, 0.0F, 0.0F)));
        addQuestTask(new QuestTask("test3", "asdasdasd3", new Vector3f(0.0F, 0.0F, 0.0F)));

    }
}
