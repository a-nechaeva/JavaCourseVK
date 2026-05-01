package org.example.boot.web;

import org.example.boot.domain.User;
import org.example.boot.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestParam String name,
            @RequestParam String skills,
            @RequestParam int experience) {
        User user = new User(name, skills, experience);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name) {
        return userRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}