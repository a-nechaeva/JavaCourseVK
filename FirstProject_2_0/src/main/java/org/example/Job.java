package org.example;

import java.util.*;

public class Job {
    private String title;
    private String company;
    private Set<String> tags = new TreeSet<>();
    private Integer experience;

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
    public Set<String> getTags() {
        return tags;
    }
    public Integer getExperience() {
        return experience;
    }
    public String toString() {
        return title + " at " + company;
    }

    public boolean equals(Object obj) {
        Job job = (Job) obj;
        return Objects.equals(title, job.title);
    }
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
