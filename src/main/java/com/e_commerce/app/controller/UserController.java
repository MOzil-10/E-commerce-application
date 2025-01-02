package com.e_commerce.app.controller;

import com.e_commerce.app.model.User;
import com.e_commerce.app.request.LoginRequest;
import com.e_commerce.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user.
     *
     * @param user The user to be registered.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Login user and generate a JWT token.
     *
     * @param loginRequest The login request containing the email and password.
     * @return A ResponseEntity containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.findUserByEmail(loginRequest.getEmail());
        if (user.isPresent() && user.get().getPasswords().equals(loginRequest.getPassword())) {
            String token = userService.generateToken(user.get());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Find a user by their email address.
     *
     * @param email The email of the user.
     * @return A ResponseEntity containing the user if found.
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update user information.
     *
     * @param userId The ID of the user to update.
     * @param updatedUser The updated user information.
     * @return The updated user.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User updatedUser) {
        User updatedUserResult = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(updatedUserResult);
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @return A response entity indicating the result.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}