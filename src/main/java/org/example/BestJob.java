package org.example;

import java.util.*;

public class BestJob implements Runnable {
    private final Set<User> users;
    private final Set<Job> jobs;

    public BestJob(Set<User> users, Set<Job> jobs) {
        this.users = users;
        this.jobs = jobs;
    }

    @Override
    public void run() {
        Set<User> usersCopy;
        Set<Job> jobsCopy;

        synchronized (users) {
            usersCopy = new HashSet<>(users);
        }
        synchronized (jobs) {
            jobsCopy = new HashSet<>(jobs);
        }

        for (User user : usersCopy) {
            Job best = findBestJob(user, jobsCopy);
            if (best != null) {
                System.out.printf("%s, лучшее предложение — %s%n",
                        user.getName(), best.toString());
            }
        }
    }

    private Job findBestJob(User user, Set<Job> jobs) {
        return jobs.stream()
                .map(job -> new Match(user, job))
                .filter(m -> m.getSuggested() != null && m.getSuggested() > 0)
                .max(Comparator.comparingInt(Match::getSuggested))
                .map(Match::getJob)
                .orElse(null);
    }
}