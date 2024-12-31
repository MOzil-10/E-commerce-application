package com.e_commerce.app.service;

import com.e_commerce.app.enums.Role;
import com.e_commerce.app.model.User;
import com.e_commerce.app.repository.UserRepository;
import com.e_commerce.app.service.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user.
     *
     * @param user The user to be registered.
     * @return The registered user.
     */
    public User registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }

        user.setPasswords(passwordEncoder.encode(user.getPasswords()));
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email of the user to find.
     * @return An optional containing the user if found.
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom to generate the token.
     * @return The JWT token.
     */
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The token to validate.
     * @param user The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, User user) {
        return jwtService.isTokenValid(token, user);
    }

    /**
     * Updates user information.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated user information.
     * @return The updated user.
     */
    public User updateUser(int userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAddress(updatedUser.getAddress());
        if (updatedUser.getPasswords() != null && !updatedUser.getPasswords().isEmpty()) {
            existingUser.setPasswords(passwordEncoder.encode(updatedUser.getPasswords()));
        }
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     */
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
