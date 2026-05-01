package org.example.boot.web;

import org.example.boot.domain.Job;
import org.example.boot.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @PostMapping
    public ResponseEntity<Job> createJob(
            @RequestParam String title,
            @RequestParam String company,
            @RequestParam String tags,
            @RequestParam int experience) {
        Job job = new Job(title, company, tags, experience);
        jobRepository.save(job);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Job> getJob(@PathVariable String title) {
        return jobRepository.findByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll().stream()
                .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();
        return ResponseEntity.ok(jobs);
    }
}