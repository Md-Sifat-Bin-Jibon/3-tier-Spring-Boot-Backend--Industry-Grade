package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int USERNAME_LENGTH = 16;
    private final SecureRandom random = new SecureRandom();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new AuthResponse(false, "Email already exists");
        }

        // Generate unique username (16 characters)
        String username = generateUniqueUsername();

        // Hash the password
        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Create and save user
        User user = new User(
            signupRequest.getEmail(),
            hashedPassword,
            username
        );
        
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());

        return new AuthResponse(
            true,
            "User registered successfully",
            username,
            user.getEmail(),
            user.getRole(), // Role can be null initially
            accessToken,
            refreshToken
        );
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            return new AuthResponse(false, "Invalid email or password");
        }

        User user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid email or password");
        }

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());

        return new AuthResponse(
            true,
            "Login successful",
            user.getUsername(),
            user.getEmail(),
            user.getRole(), // Role can be null initially
            accessToken,
            refreshToken
        );
    }

    /**
     * Generates a unique 16-character username
     * Retries if generated username already exists
     */
    private String generateUniqueUsername() {
        String username;
        int maxAttempts = 100;
        int attempts = 0;

        do {
            username = generateRandomUsername();
            attempts++;
            
            if (attempts > maxAttempts) {
                throw new RuntimeException("Unable to generate unique username after " + maxAttempts + " attempts");
            }
        } while (userRepository.existsByUsername(username));

        return username;
    }

    /**
     * Generates a random 16-character string
     */
    private String generateRandomUsername() {
        StringBuilder sb = new StringBuilder(USERNAME_LENGTH);
        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
