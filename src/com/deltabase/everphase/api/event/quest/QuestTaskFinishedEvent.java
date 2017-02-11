package com.deltabase.everphase.api.event.quest;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.quest.Quest;
import com.deltabase.everphase.quest.QuestTask;

public class QuestTaskFinishedEvent extends Event {

    private Quest quest;
    private QuestTask questTask;

    public QuestTaskFinishedEvent(Quest quest, QuestTask questTask) {
        this.quest = quest;
        this.questTask = questTask;
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestTask getQuestTask() {
        return questTask;
    }
}
