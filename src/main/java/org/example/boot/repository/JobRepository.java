package org.example.boot.repository;

import org.example.boot.domain.Job;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JobRepository {
    private final Map<String, Job> jobs = new ConcurrentHashMap<>();

    public Job save(Job job) {
        jobs.put(job.getTitle(), job);
        return job;
    }

    public Optional<Job> findByTitle(String title) {
        return Optional.ofNullable(jobs.get(title));
    }

    public List<Job> findAll() {
        return new ArrayList<>(jobs.values());
    }

    public Set<Job> findAllAsSet() {
        return new LinkedHashSet<>(jobs.values());
    }

    public boolean existsByTitle(String title) {
        return jobs.containsKey(title);
    }
}