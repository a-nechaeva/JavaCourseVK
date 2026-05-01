package org.example.boot.domain;

import java.util.*;

public class User {
    private String name;
    private Set<String> skills = new TreeSet<>();
    private Integer experience;

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

    public String toString() {
        return name + " " + String.join(",", skills) + " " + experience;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return Objects.equals(name, user.name);
    }
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
