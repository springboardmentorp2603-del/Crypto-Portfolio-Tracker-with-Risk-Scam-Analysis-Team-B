package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.model.User;
import com.CryptoProject.CryptoInfosys.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void changePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void setPreferences(String email, Map<String, String> preferences) {
        User user = getUserByEmail(email);
        user.setPreferences(preferences);
        userRepository.save(user);
    }
}
