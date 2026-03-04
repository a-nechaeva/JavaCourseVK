package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Job {
    String title;
    String company;
    List<String> tags = new ArrayList<>();
    Integer experience;

    public Job(String title, String company, String tags, int experience) {
        this.title = title;
        this.company = company;
        setTags(tags);
        this.experience = experience;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public void setTags(String tagString) {
       String[] tagSet = tagString.split(",");
        Collections.addAll(this.tags, tagSet);
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }
    public List<String> getTags() {
        return tags;
    }
    public Integer getExperience() {
        return experience;
    }
}
