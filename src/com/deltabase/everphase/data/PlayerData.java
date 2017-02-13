package com.deltabase.everphase.data;

import com.deltabase.everphase.quest.Quest;
import com.deltabase.everphase.skill.Skill;

import java.util.HashMap;
import java.util.Map;

public class PlayerData extends SDO {

    public Map<String, Skill> skillLevels = new HashMap<>();

    public Map<String, Quest> notStartedQuests = new HashMap<>();
    public Map<String, Quest> startedQuests = new HashMap<>();
    public Map<String, Quest> finishedQuests = new HashMap<>();

}
