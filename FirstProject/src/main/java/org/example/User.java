package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    String name;
    List<String> skills;
    Integer experience;

    public void setName(String name) {
        this.name = name;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setSkills(String skillString) {
        String[] skillSet = skillString.split(",");
        Collections.addAll(this.skills, skillSet);
    }
}
