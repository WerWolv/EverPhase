package com.deltabase.everphase.data;

import com.deltabase.everphase.quest.Quest;
import com.deltabase.everphase.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    public List<Skill> skillLevels = new ArrayList<>();

    public List<Quest> notStartedQuests = new ArrayList<>();
    public List<Quest> startedQuests = new ArrayList<>();
    public List<Quest> finishedQuests = new ArrayList<>();

}
