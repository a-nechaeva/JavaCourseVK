package org.example.boot.service;

import org.example.boot.domain.Job;
import org.example.boot.domain.Match;
import org.example.boot.domain.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    /**
     * Рассчитать score соответствия пользователя вакансии
     * @return количество совпавших навыков (с учетом опыта)
     */
    public int calculateMatchScore(User user, Job job) {
        Match match = new Match(user, job);
        return match.getSuggested();
    }

    /**
     * Найти топ-N лучших вакансий для пользователя
     * @param user пользователь
     * @param jobs список всех вакансий
     * @param limit максимальное количество результатов
     * @return список вакансий с score, отсортированный по убыванию
     */
    public List<Map.Entry<Job, Integer>> findTopJobsForUser(User user, Set<Job> jobs, int limit) {
        Map<Job, Integer> jobMatching = new HashMap<>();

        for (Job job : jobs) {
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

    /**
     * Найти лучшую вакансию для пользователя
     * @return вакансия с максимальным score или null
     */
    public Job findBestJobForUser(User user, Set<Job> jobs) {
        return jobs.stream()
                .map(job -> new Match(user, job))
                .filter(m -> m.getSuggested() > 0)
                .max(Comparator.comparingInt(Match::getSuggested))
                .map(Match::getJob)
                .orElse(null);
    }

    /**
     * Посчитать количество подходящих вакансий для пользователя
     */
    public int countMatchingJobsForUser(User user, Set<Job> jobs) {
        return (int) jobs.stream()
                .filter(job -> calculateMatchScore(user, job) > 0)
                .count();
    }

    /**
     * Найти всех пользователей, которым подходит данная вакансия
     */
    public List<Map.Entry<User, Integer>> findMatchingUsersForJob(Job job, Set<User> users, int limit) {
        Map<User, Integer> userMatching = new HashMap<>();

        for (User user : users) {
            int score = calculateMatchScore(user, job);
            if (score > 0) {
                userMatching.put(user, score);
            }
        }

        return userMatching.entrySet().stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Получить детальную информацию о соответствии
     */
    public MatchDetails getMatchDetails(User user, Job job) {
        int matchingSkills = countMatchingSkills(user, job);
        int totalJobSkills = job.getTags().size();
        int score = calculateMatchScore(user, job);
        boolean experienceMatch = user.getExperience() >= job.getExperience();

        return new MatchDetails(
                user.getName(),
                job.getTitle(),
                matchingSkills,
                totalJobSkills,
                score,
                experienceMatch,
                getCommonSkills(user, job)
        );
    }

    /**
     * Посчитать количество совпавших навыков
     */
    private int countMatchingSkills(User user, Job job) {
        int count = 0;
        for (String skill : user.getSkills()) {
            if (job.getTags().contains(skill)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Получить список общих навыков
     */
    private Set<String> getCommonSkills(User user, Job job) {
        Set<String> common = new HashSet<>(user.getSkills());
        common.retainAll(job.getTags());
        return common;
    }

    /**
     * Класс для хранения детальной информации о соответствии
     */
    public static class MatchDetails {
        private final String userName;
        private final String jobTitle;
        private final int matchingSkills;
        private final int totalJobSkills;
        private final int score;
        private final boolean experienceMatch;
        private final Set<String> commonSkills;

        public MatchDetails(String userName, String jobTitle, int matchingSkills,
                            int totalJobSkills, int score, boolean experienceMatch,
                            Set<String> commonSkills) {
            this.userName = userName;
            this.jobTitle = jobTitle;
            this.matchingSkills = matchingSkills;
            this.totalJobSkills = totalJobSkills;
            this.score = score;
            this.experienceMatch = experienceMatch;
            this.commonSkills = commonSkills;
        }

        // Геттеры
        public String getUserName() { return userName; }
        public String getJobTitle() { return jobTitle; }
        public int getMatchingSkills() { return matchingSkills; }
        public int getTotalJobSkills() { return totalJobSkills; }
        public int getScore() { return score; }
        public boolean isExperienceMatch() { return experienceMatch; }
        public Set<String> getCommonSkills() { return commonSkills; }

        @Override
        public String toString() {
            return String.format("Match: %s -> %s\n" +
                            "  Score: %d\n" +
                            "  Matching skills: %d/%d\n" +
                            "  Common skills: %s\n" +
                            "  Experience match: %s",
                    userName, jobTitle, score, matchingSkills, totalJobSkills,
                    commonSkills, experienceMatch ? "Yes" : "No");
        }
    }
}
