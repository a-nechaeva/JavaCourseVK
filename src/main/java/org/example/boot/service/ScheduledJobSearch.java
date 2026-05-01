package org.example.boot.service;

import org.example.boot.domain.Job;
import org.example.boot.domain.User;
import org.example.boot.repository.JobRepository;
import org.example.boot.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ScheduledJobSearch {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ScheduledJobSearch(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void findBestJobsForAllUsers() {
        List<User> users = userRepository.findAll();
        List<Job> jobs = jobRepository.findAll();

        for (User user : users) {
            Job bestJob = findBestJobForUser(user, jobs);
            if (bestJob != null) {
                System.out.printf("%s, лучшее предложение — %s%n",
                        user.getName(), bestJob.toString());
            }
        }
    }

    private Job findBestJobForUser(User user, List<Job> jobs) {
        return jobs.stream()
                .map(job -> new JobMatch(user, job))
                .filter(match -> match.getScore() > 0)
                .max(Comparator.comparingInt(JobMatch::getScore))
                .map(JobMatch::getJob)
                .orElse(null);
    }

    private static class JobMatch {
        private final User user;
        private final Job job;
        private final int score;

        public JobMatch(User user, Job job) {
            this.user = user;
            this.job = job;
            this.score = calculateScore(user, job);
        }

        private int calculateScore(User user, Job job) {
            int count = 0;
            for (String skill : user.getSkills()) {
                if (job.getTags().contains(skill)) {
                    count++;
                }
            }
            if (user.getExperience() < job.getExperience()) {
                count /= 2;
            }
            return count;
        }

        public User getUser() { return user; }
        public Job getJob() { return job; }
        public int getScore() { return score; }
    }
}