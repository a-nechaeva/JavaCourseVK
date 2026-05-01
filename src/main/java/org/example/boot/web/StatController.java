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
@RequestMapping("/api/stats")
public class StatController {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public StatController(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/experience")
    public ResponseEntity<List<Job>> getJobsByMinExperience(@RequestParam int minExperience) {
        List<Job> jobs = jobRepository.findAll().stream()
                .filter(job -> job.getExperience() >= minExperience)
                .sorted(Comparator.comparing(Job::getTitle))
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/users-matching")
    public ResponseEntity<List<User>> getUsersWithMinMatches(@RequestParam int minMatches) {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> countMatchingJobs(user) >= minMatches)
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/top-skills")
    public ResponseEntity<List<String>> getTopSkills(@RequestParam int limit) {
        Map<String, Long> skillCounts = new HashMap<>();
        for (User user : userRepository.findAll()) {
            for (String skill : user.getSkills()) {
                skillCounts.put(skill, skillCounts.getOrDefault(skill, 0L) + 1);
            }
        }

        List<String> topSkills = skillCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok(topSkills);
    }

    private int countMatchingJobs(User user) {
        int count = 0;
        for (Job job : jobRepository.findAll()) {
            int matchingSkills = 0;
            for (String skill : user.getSkills()) {
                if (job.getTags().contains(skill)) {
                    matchingSkills++;
                }
            }
            if (user.getExperience() >= job.getExperience() && matchingSkills > 0) {
                count++;
            } else if (user.getExperience() < job.getExperience() && matchingSkills > 0) {
                count++;
            }
        }
        return count;
    }
}