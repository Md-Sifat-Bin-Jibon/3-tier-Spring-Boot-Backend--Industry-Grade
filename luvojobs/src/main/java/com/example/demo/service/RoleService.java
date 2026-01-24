package com.example.demo.service;

import com.example.demo.dto.RoleResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final UserRepository userRepository;

    public RoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get the current role of a user by email
     */
    public RoleResponse getCurrentRole(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return new RoleResponse(false, "User not found");
        }

        User user = userOptional.get();
        return new RoleResponse(
            true,
            "Role retrieved successfully",
            user.getRole(),
            user.getEmail(),
            user.getUsername()
        );
    }

    /**
     * Update the role of a user by email
     */
    public RoleResponse updateRole(String email, UserRole newRole) {
        if (newRole == null) {
            return new RoleResponse(false, "Role cannot be null");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return new RoleResponse(false, "User not found");
        }

        User user = userOptional.get();
        user.setRole(newRole);
        userRepository.save(user);

        return new RoleResponse(
            true,
            "Role updated successfully",
            user.getRole(),
            user.getEmail(),
            user.getUsername()
        );
    }
}
