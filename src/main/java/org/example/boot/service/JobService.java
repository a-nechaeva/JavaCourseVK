package org.example.boot.service;

import org.example.boot.domain.Job;
import org.example.boot.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Создать новую вакансию
     */
    public Job createJob(String title, String company, String tags, int experience) {
        Job job = new Job(title, company, tags, experience);
        return jobRepository.save(job);
    }

    /**
     * Найти вакансию по названию
     */
    public Optional<Job> getJobByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    /**
     * Получить все вакансии
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Получить все вакансии как Set (для совместимости с legacy кодом)
     */
    public Set<Job> getAllJobsAsSet() {
        return jobRepository.findAllAsSet();
    }

    /**
     * Проверить существует ли вакансия
     */
    public boolean existsJob(String title) {
        return jobRepository.existsByTitle(title);
    }

    /**
     * Обновить компанию в вакансии
     */
    public Job updateJobCompany(String title, String newCompany) {
        return jobRepository.findByTitle(title)
                .map(job -> {
                    job.setCompany(newCompany);
                    return jobRepository.save(job);
                })
                .orElseThrow(() -> new RuntimeException("Job not found: " + title));
    }

    /**
     * Обновить требования к опыту
     */
    public Job updateJobExperience(String title, int newExperience) {
        return jobRepository.findByTitle(title)
                .map(job -> {
                    job.setExperience(newExperience);
                    return jobRepository.save(job);
                })
                .orElseThrow(() -> new RuntimeException("Job not found: " + title));
    }

    /**
     * Обновить теги/навыки
     */
    public Job updateJobTags(String title, String newTags) {
        return jobRepository.findByTitle(title)
                .map(job -> {
                    job.setTags(newTags);
                    return jobRepository.save(job);
                })
                .orElseThrow(() -> new RuntimeException("Job not found: " + title));
    }

    /**
     * Найти вакансии с опытом не меньше указанного
     */
    public List<Job> findJobsByMinExperience(int minExperience) {
        return jobRepository.findAll().stream()
                .filter(job -> job.getExperience() >= minExperience)
                .toList();
    }
}