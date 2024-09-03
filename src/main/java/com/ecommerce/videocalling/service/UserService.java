package com.ecommerce.videocalling.service;

import com.ecommerce.videocalling.model.User;
import com.ecommerce.videocalling.repo.UserRepo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private  final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;


    public ResponseEntity<ObjectNode> register(User user) {
        ObjectNode node = objectMapper.createObjectNode();

        // Check if the username already exists
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            log.info("Username already exists");
            node.put("message", "Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(node);
        }

        // Check if the email already exists
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            log.info("Email already exists");
            node.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(node);
        }

        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            log.info("Invalid email format");
            node.put("message", "Invalid email format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(node);
        }

        // Validate password strength (implement your own logic or use a library)
        if (!isValidPassword(user.getPassword())) {
            log.info("Weak password");
            node.put("message", "Weak password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(node);
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        // Create a new user entity
        User newuser = User.builder()
                .username(user.getUsername())
                .password(hashedPassword)
                .email(user.getEmail())
                .status("online")  // Set default status to offline
                .build();

        // Save the new user
        userRepo.save(newuser);

        // Successful registration
        node.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(node);
    }

    // Example of a simple email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Example of a simple password validation method
    private boolean isValidPassword(String password) {
        // Implement your own password strength validation logic
        // For example, check for minimum length, inclusion of digits, special characters, etc.
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[@#$%^&+=].*");
    }


    public User login(User user) {
        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());

        if (optionalUser.isPresent()) {
            User getUser = optionalUser.get();

            // Assuming passwords are hashed, use passwordEncoder.matches() for comparison
            if (passwordEncoder.matches(user.getPassword(), getUser.getPassword())) {
                getUser.setStatus("online");
                userRepo.save(getUser);
                return getUser;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
    }


    public User logout(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        User getUser = optionalUser.get();
        getUser.setStatus("offline");
        return userRepo.save(getUser);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
}