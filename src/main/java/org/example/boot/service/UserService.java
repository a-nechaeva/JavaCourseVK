package org.example.boot.service;

import org.example.boot.domain.User;
import org.example.boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создать нового пользователя
     */
    public User createUser(String name, String skills, int experience) {
        User user = new User(name, skills, experience);
        return userRepository.save(user);
    }

    /**
     * Найти пользователя по имени
     */
    public Optional<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * Получить всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получить всех пользователей как Set (для совместимости с legacy кодом)
     */
    public Set<User> getAllUsersAsSet() {
        return userRepository.findAllAsSet();
    }

    /**
     * Проверить существует ли пользователь
     */
    public boolean existsUser(String name) {
        return userRepository.existsByName(name);
    }

    /**
     * Обновить навыки пользователя
     */
    public User updateUserSkills(String name, String newSkills) {
        return userRepository.findByName(name)
                .map(user -> {
                    user.setSkills(newSkills);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found: " + name));
    }

    /**
     * Обновить опыт пользователя
     */
    public User updateUserExperience(String name, int newExperience) {
        return userRepository.findByName(name)
                .map(user -> {
                    user.setExperience(newExperience);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found: " + name));
    }
}
