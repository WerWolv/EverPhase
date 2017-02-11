package com.deltabase.everphase.quest;

import org.joml.Vector3f;

public class QuestTask {

    private String taskName = "";
    private String taskDescription = "";
    private Vector3f location = new Vector3f(0.0F);

    public QuestTask(String taskName, String taskDescription, Vector3f location) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.location = location;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Vector3f getLocation() {
        return location;
    }
}
