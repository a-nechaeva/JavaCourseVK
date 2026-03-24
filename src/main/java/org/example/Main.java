package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static Set<Job> jobSet = new LinkedHashSet<>();
    static Set<User> userSet = new LinkedHashSet<>();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String curLine;

        //Here we are trying to execute commands from the file only create features

        // at the start point
        // next we read, execute and write down to the file input commands without exit command
        // if we meet history command we print commands from the file
        while (!(curLine = in.nextLine()).equals("exit")){
            executeCommand(curLine);
        }
        System.exit(0);
    }

    public static void executeCommand(String command) {

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
                    for (Job j : jobSet) System.out.println(j.toString());
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
                //suggest <username>
                String userName = command.substring(8);

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
}