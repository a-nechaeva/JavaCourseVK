package org.example.boot;

import org.example.boot.BestJob;
import org.example.boot.domain.Job;
import org.example.boot.domain.Match;
import org.example.boot.domain.User;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.*;

public class Main {
    static Set<Job> jobSet = new LinkedHashSet<>();
    static Set<User> userSet = new LinkedHashSet<>();
    static ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String curLine;

        String logFileName = "commands.txt";
        File logFile = new File(logFileName);

        try {
            if (logFile.exists()) {
                if (logFile.length() > 0) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty()) executeFromFile(line);
                        }
                    }
                }
            } else {
                logFile.createNewFile();
            }
        } catch (IOException e) {}


        scheduler = Executors.newScheduledThreadPool(1);
        BestJob suggestionTask = new BestJob(userSet, jobSet);

        scheduler.scheduleAtFixedRate(suggestionTask, 0, 60, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        while (!(curLine = in.nextLine()).equals("exit")){
            executeCommand(curLine);
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                if (!curLine.trim().equals("exit")) writer.println(curLine);
            } catch (IOException e) {}

        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    public static void executeFromFile(String command) {
        if (command.charAt(0) == 'u') {
            // user
            if (command.charAt(4) != '-')  {
                // user alice --skills=java,ml,linux --exp=2
                String[] userString = command.split("\\s+");
                String name = userString[1];
                String experience;
                String skills;

                if (userString[2].charAt(2) == 's') {
                    skills = userString[2].substring(9);
                    experience = userString[3].substring(6);
                } else {
                    skills = userString[3].substring(9);
                    experience = userString[2].substring(6);
                }
                userSet.add(new User(name, skills, Integer.parseInt(experience)));
            }
        } else {
            if (command.charAt(0) == 'j') {
                // job
                if (command.charAt(3) != '-')  {
                    // job Backend_Dev --company=VK --tags=java,backend,linux --exp=1
                    String[] jobString = command.split("\\s+");
                    String title = jobString[1];
                    String company = null;
                    String tags = null;
                    String experience = null;
                    for (String s : jobString) {
                        if (s.charAt(2) == 'c') company = s.substring(10);
                        if (s.charAt(2) == 't') tags = s.substring(7);
                        if (s.charAt(2) == 'e') experience = s.substring(6);
                    }
                    jobSet.add(new Job(title, company, tags, Integer.parseInt(experience)));
                }
            }
        }
    }

    public static void executeCommand(String command) {
        if (command.charAt(0) == 'h') {
            // here we print commands from the file
            File logFile = new File("commands.txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {}
            return;
        }
        if (command.charAt(0) == 'u') {
            // user
            if (command.charAt(4) == '-') {
                // user-list
                for (User u: userSet) System.out.println(u.toString());
            } else {
                // user alice --skills=java,ml,linux --exp=2
                String[] userString = command.split("\\s+");
                String name = userString[1];
                String experience;
                String skills;

                if (userString[2].charAt(2) == 's') {
                    skills = userString[2].substring(9);
                    experience = userString[3].substring(6);
                } else {
                    skills = userString[3].substring(9);
                    experience = userString[2].substring(6);
                }
                userSet.add(new User(name, skills, Integer.parseInt(experience)));
            }
        } else {
            if (command.charAt(0) == 'j') {
                // job
                if (command.charAt(3) == '-') {
                    // job-list
                    //for (Job j : jobSet) System.out.println(j.toString());
                    jobSet.stream()
                            .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                            .forEach(System.out::println);
                } else {
                    // job Backend_Dev --company=VK --tags=java,backend,linux --exp=1
                    String[] jobString = command.split("\\s+");
                    String title = jobString[1];
                    String company = null;
                    String tags = null;
                    String experience = null;
                    for (String s : jobString) {
                        if (s.charAt(2) == 'c') company = s.substring(10);
                        if (s.charAt(2) == 't') tags = s.substring(7);
                        if (s.charAt(2) == 'e') experience = s.substring(6);
                    }
                    jobSet.add(new Job(title, company, tags, Integer.parseInt(experience)));
                }
            } else {
                if (command.charAt(1) == 'u') {
                    //suggest <username>
                    String userName = command.substring(8);

                    for (User u : userSet) {
                        if (u.getName().equals(userName)) {
                            User curUser = u;
                            Map<Job, Integer> jobMatching = new HashMap<>();
                            for (Job j : jobSet) {
                                Match curMatch = new Match(curUser, j);
                                jobMatching.put(j, curMatch.getSuggested());
                            }
                            List<Map.Entry<Job, Integer>> topVacancy = jobMatching.entrySet()
                                    .stream()
                                    .filter(entry -> entry.getValue() != null && entry.getValue() > 0)
                                    .sorted(Map.Entry.<Job, Integer>comparingByValue().reversed())
                                    .limit(2)
                                    .collect(Collectors.toList());

                            if (!topVacancy.isEmpty()) {
                                for (Map.Entry<Job, Integer> entry : topVacancy) {
                                    System.out.println(entry.getKey().toString());

                                }
                            }
                        }
                    }
                } else {
                    // stat

                    if (command.charAt(7) == 'e') {
                        int val = Integer.parseInt(command.substring(11));
                        jobSet.stream()
                                .filter(j -> j.getExperience() >= val)
                                .sorted(Comparator.comparing(Job::getTitle))
                                .forEach(System.out::println);
                    }else if (command.charAt(7) == 'm') {
                        int val = Integer.parseInt(command.substring(13));
                        Map<User, Integer> userMatchCounts = new HashMap<>();

                        for (User u : userSet) {
                            int count = 0;
                            for (Job j : jobSet) {
                                Match m = new Match(u, j);
                                if (m.getSuggested() > 0) {
                                    count++;
                                }
                            }
                            userMatchCounts.put(u, count);
                        }

                        userMatchCounts.entrySet().stream()
                                .filter(e -> e.getValue() >= val)
                                .map(Map.Entry::getKey)
                                .sorted(Comparator.comparing(User::getName))
                                .forEach(System.out::println);
                    }
                    else if (command.charAt(7) == 't') {
                        int val = Integer.parseInt(command.substring(18));
                        Map<String, Long> skillCounts = new HashMap<>();
                        for (User u : userSet) {
                            for (String skill : u.getSkills()) {
                                skillCounts.put(skill, skillCounts.getOrDefault(skill, 0L) + 1);
                            }
                        }

                        List<Map.Entry<String, Long>> sortedSkills = skillCounts.entrySet().stream()
                                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                .collect(Collectors.toList());

                        List<String> topSkills = sortedSkills.stream()
                                .limit(val)
                                .map(Map.Entry::getKey)
                                .sorted()
                                .collect(Collectors.toList());

                        for (String s : topSkills) {
                            System.out.println(s);
                        }
                    }
                }
            }
        }
    }
}