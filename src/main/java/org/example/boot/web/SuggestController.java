package org.example.boot.web;

import org.example.boot.domain.Job;
import org.example.boot.domain.User;
import org.example.boot.repository.JobRepository;
import org.example.boot.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public SuggestController(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/{userName}")
    public ResponseEntity<List<Job>> suggestJobsForUser(@PathVariable String userName) {
        return userRepository.findByName(userName)
                .map(user -> {
                    List<Map.Entry<Job, Integer>> topJobs = findTopJobsForUser(user, 2);
                    List<Job> suggestedJobs = topJobs.stream()
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(suggestedJobs);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private List<Map.Entry<Job, Integer>> findTopJobsForUser(User user, int limit) {
        Map<Job, Integer> jobMatching = new HashMap<>();

        for (Job job : jobRepository.findAll()) {
            int score = calculateMatchScore(user, job);
            if (score > 0) {
                jobMatching.put(job, score);
            }
        }

        return jobMatching.entrySet().stream()
                .sorted(Map.Entry.<Job, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private int calculateMatchScore(User user, Job job) {
        int countMatching = 0;
        Set<String> userSkills = user.getSkills();
        Set<String> jobTags = job.getTags();

        for (String skill : userSkills) {
            if (jobTags.contains(skill)) {
                countMatching++;
            }
        }

        if (user.getExperience() < job.getExperience()) {
            countMatching /= 2;
        }

        return countMatching;
    }
}