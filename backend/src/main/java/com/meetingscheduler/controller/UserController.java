package com.meetingscheduler.controller;

import com.meetingscheduler.dto.*;
import com.meetingscheduler.model.User;
import com.meetingscheduler.service.UserService;
import com.meetingscheduler.config.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Get all users (for debugging/admin only)
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    // ✅ Update username (returns fresh JWT)
    @PutMapping("/{id}")
    public LoginResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        User updated = userService.updateUser(id, req);

        // generate new JWT with updated username
        String newToken = jwtUtil.generateToken(updated.getUsername());

        return new LoginResponse(newToken, updated.getId(), updated.getUsername());
    }

    // ✅ Change password (returns fresh JWT)
    @PutMapping("/{id}/change-password")
    public LoginResponse changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest req) {
        User user = userService.getById(id);

        // check old password
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        // set new password
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userService.save(user);

        // return fresh JWT
        String newToken = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(newToken, user.getId(), user.getUsername());
    }

    // ✅ Register new user
    @PostMapping("/register")
    public LoginResponse registerUser(@RequestBody UserRegisterRequest req) {
        User saved = userService.register(req);

        // return token directly after registration
        String token = jwtUtil.generateToken(saved.getUsername());
        return new LoginResponse(token, saved.getId(), saved.getUsername());
    }
}