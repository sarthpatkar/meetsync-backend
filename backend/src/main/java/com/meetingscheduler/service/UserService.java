package com.meetingscheduler.service;

import com.meetingscheduler.dto.UpdateUserRequest;
import com.meetingscheduler.dto.UserRegisterRequest;
import com.meetingscheduler.model.User;
import com.meetingscheduler.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register new user
    public User register(UserRegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return userRepository.save(user);
    }

    // ✅ Get all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // ✅ Find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // ✅ Update user (username & password)
    public User updateUser(Long id, UpdateUserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            userRepository.findByUsername(req.getUsername())
                    .filter(u -> !u.getId().equals(id)) // ✅ allow same user to keep their username
                    .ifPresent(u -> { throw new RuntimeException("Username already exists"); });

            user.setUsername(req.getUsername());
        }

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword())); // ✅ hash new password
        }

        return userRepository.save(user);
    }

    // ✅ NEW: Get user by ID
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ NEW: Save user
    public User save(User user) {
        return userRepository.save(user);
    }
}
