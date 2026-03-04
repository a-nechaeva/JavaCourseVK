package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String curLine;
        Set<Job> jobSet = new HashSet<>();
        Set<User> userSet = new HashSet<>();

        while (!(curLine = in.nextLine()).equals("exit")){

            if (curLine.charAt(0) == 'u') {
                // user
                if (curLine.charAt(4) == '-') {
                    // user-list
                    for (User u: userSet) System.out.println(u.toString());
                } else {
                    // user alice --skills=java,ml,linux --exp=2
                    String[] userString = curLine.split("\\s+");
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
                    if (curLine.charAt(0) == 'j') {
                    // job
                    if (curLine.charAt(4) == '-') {
                        // job-list
                        for (Job j : jobSet) System.out.println(j.toString());
                    } else {
                        // job Backend_Dev --company=VK --tags=java,backend,linux --exp=1
                        String[] jobString = curLine.split("\\s+");
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
                        //suggest <username>
                        String userName = curLine.substring(8);

                        for (User u: userSet) {
                            if (u.getName().equals(userName)){
                                User curUser = u;
                                Map<Job, Integer> jobMatching = new HashMap<>();
                                for (Job j: jobSet) {
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
                    }
                }

        }
        System.exit(0);
    }
}