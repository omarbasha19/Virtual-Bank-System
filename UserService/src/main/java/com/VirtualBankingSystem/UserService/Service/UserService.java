package com.VirtualBankingSystem.UserService.Service;

import com.VirtualBankingSystem.UserService.Entity.User;
import com.VirtualBankingSystem.UserService.Exceptions.UserAlreadyExistsException;
import com.VirtualBankingSystem.UserService.Exceptions.UserNotFound;
import com.VirtualBankingSystem.UserService.Repository.UserRepositoryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepositoryI userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with this username or email already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFound("User with ID " + userId + " not found.");
        }
        return user;
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new UserNotFound("Invalid username or password.");
        }
        return user;
    }
}
