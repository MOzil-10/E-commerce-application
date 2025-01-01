package com.e_commerce.app.userServiceTest;

import com.e_commerce.app.model.User;
import com.e_commerce.app.repository.UserRepository;
import com.e_commerce.app.service.jwt.JwtService;
import com.e_commerce.app.service.UserService;
import com.e_commerce.app.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserWithDefaultRoleCustomer() {
        User user = new User();
        user.setUsername("Mukosi Budeli");
        user.setEmail("mukosi.co.za");
        user.setPasswords("password");
        user.setRole(null);
        user.setAddress("334 Main St, Parktown, JHB");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPasswords())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);  // Mock save to return the user

        User registeredUser = userService.registerUser(user);

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(registeredUser);  // Ensure that registeredUser is not null
        assertEquals("encodedPassword", registeredUser.getPasswords());
        assertEquals("CUSTOMER", registeredUser.getRole().name());
    }

    @Test
    void testRegisterUserWithRoleAdmin() {
        User user = new User();
        user.setUsername("Admin User");
        user.setEmail("admin@ecommerce.com");
        user.setPasswords("adminpassword");
        user.setRole(Role.ADMIN);
        user.setAddress("123 Admin St, Admin City");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("adminpassword")).thenReturn("encodedAdminPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).encode("adminpassword");
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(registeredUser);
        assertEquals("encodedAdminPassword", registeredUser.getPasswords());
        assertEquals("ADMIN", registeredUser.getRole().name());
    }


    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserByEmail(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testGenerateToken() {
        User user = new User();
        user.setEmail("test@example.com");

        when(jwtService.generateToken(user)).thenReturn("generatedToken");

        String token = userService.generateToken(user);

        verify(jwtService, times(1)).generateToken(user);
        assertEquals("generatedToken", token);
    }

    @Test
    void testValidateTokenValid() {
        User user = new User();
        String token = "validToken";

        when(jwtService.isTokenValid(token, user)).thenReturn(true);

        boolean isValid = userService.validateToken(token, user);

        verify(jwtService, times(1)).isTokenValid(token, user);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        User user = new User();
        String token = "invalidToken";

        when(jwtService.isTokenValid(token, user)).thenReturn(false);

        boolean isValid = userService.validateToken(token, user);

        verify(jwtService, times(1)).isTokenValid(token, user);
        assertFalse(isValid);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
