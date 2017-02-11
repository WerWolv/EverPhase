package com.deltabase.everphase.api.event.quest;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.quest.Quest;

import java.util.List;

public class QuestStartMissingDependenciesEvent extends Event {

    private Quest questTriedToStart;
    private List<Quest> missingQuests;

    public QuestStartMissingDependenciesEvent(Quest questTriedToStart, List<Quest> missingQuests) {
        this.questTriedToStart = questTriedToStart;
        this.missingQuests = missingQuests;
    }

    public Quest getQuestTriedToStart() {
        return questTriedToStart;
    }

    public List<Quest> getMissingQuests() {
        return missingQuests;
    }
}
