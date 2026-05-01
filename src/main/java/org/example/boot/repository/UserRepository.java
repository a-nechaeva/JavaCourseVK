package org.example.boot.repository;

import org.example.boot.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User save(User user) {
        users.put(user.getName(), user);
        return user;
    }

    public Optional<User> findByName(String name) {
        return Optional.ofNullable(users.get(name));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Set<User> findAllAsSet() {
        return new LinkedHashSet<>(users.values());
    }

    public boolean existsByName(String name) {
        return users.containsKey(name);
    }
}