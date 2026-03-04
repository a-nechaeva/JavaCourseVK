package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    String name;
    List<String> skills = new ArrayList<>();
    Integer experience;

    public User(String name, String skills, int experience) {
        this.name = name;
        setSkills(skills);
        this.experience = experience;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSkills(String skillString) {
        String[] skillSet = skillString.split(",");
        Collections.addAll(this.skills, skillSet);
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public Integer getExperience() {
        return experience;
    }
}
