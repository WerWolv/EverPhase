package com.deltabase.everphase.api.event.quest;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.quest.Quest;

public class QuestStartedEvent extends Event {

    private Quest startedQuest;

    public QuestStartedEvent(Quest startedQuest) {
        this.startedQuest = startedQuest;
    }

    public Quest getStartedQuest() {
        return startedQuest;
    }
}
