package org.example;

import java.util.Set;

public class Match {
    User user;
    Job job;
    Integer suggested;

    public Match(User user, Job job) {
        this.user = user;
        this.job = job;
        setSuggested(user, job);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setSuggested(User u, Job j) {
        int countMatching = 0;
        Set<String> userSkills = u.getSkills();
        Set<String> companyTags = j.getTags();

        for (String skill: userSkills) {
            if (companyTags.contains(skill))
                countMatching++;
        }
        if (u.experience < j.experience) countMatching /= 2;
        this.suggested = countMatching;
    }

    public User getUser() {
        return user;
    }

    public Job getJob() {
        return job;
    }

    public Integer getSuggested() {
        return suggested;
    }
}
