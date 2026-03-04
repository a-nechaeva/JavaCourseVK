package org.example;

import java.util.*;

public class User {
    String name;
    Set<String> skills = new TreeSet<>();
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

    public Set<String> getSkills() {
        return skills;
    }

    public Integer getExperience() {
        return experience;
    }
}
