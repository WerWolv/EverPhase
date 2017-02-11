package com.deltabase.everphase.api.event.quest;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.quest.Quest;

public class QuestFinishedEvent extends Event {

    private Quest quest;

    public QuestFinishedEvent(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }
}
