package com.e_commerce.app.userControllerTest;

import com.e_commerce.app.controller.UserController;
import com.e_commerce.app.enums.Role;
import com.e_commerce.app.model.User;
import com.e_commerce.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId(1L);
        user.setUsername("John Moses");
        user.setEmail("john.co.za");
        user.setPasswords("encodedPassword");
    }

    @Test
    void testRegisterCustomer() throws Exception {
        User customerUser = new User();
        customerUser.setId(1L);
        customerUser.setUsername("John Moses");
        customerUser.setEmail("john.co.za");
        customerUser.setPasswords("encodedPassword");
        customerUser.setRole(Role.CUSTOMER);
        customerUser.setAddress("123 Main Street");

        when(userService.registerUser(any(User.class))).thenReturn(customerUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"John Moses\", \"email\": \"john.co.za\", \"password\": \"newPassword\"}"))
                .andDo(result -> {
                    System.out.println("Response Body: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John Moses"))
                .andExpect(jsonPath("$.email").value("john.co.za"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    void testRegisterAdmin() throws Exception {

        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("Admin User");
        adminUser.setEmail("admin@company.com");
        adminUser.setPasswords("adminPassword");
        adminUser.setRole(Role.ADMIN);
        adminUser.setAddress("123 Main Street");

        when(userService.registerUser(any(User.class))).thenReturn(adminUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"John Moses\", \"email\": \"john.co.za\", \"password\": \"newPassword\", \"address\": \"123 Main Street\"}"))
                .andDo(result -> {
                    System.out.println("Response Body: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Admin User"))
                .andExpect(jsonPath("$.email").value("admin@company.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                 .andExpect(jsonPath("$.address").value("123 Main Street"));
    }

    @Test
    void testFindUserByEmail() throws Exception {
        user.setRole(Role.CUSTOMER);

        when(userService.findUserByEmail("john.co.za")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{email}", "john.co.za"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Moses"))
                .andExpect(jsonPath("$.email").value("john.co.za"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }


    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGenerateToken() throws Exception {
        when(userService.findUserByEmail("john.co.za")).thenReturn(Optional.of(user));
        when(userService.generateToken(user)).thenReturn("fakeJwtToken");

        mockMvc.perform(post("/api/users/login")
                        .param("email", "john.co.za")
                        .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("fakeJwtToken"));
    }

    @Test
    void testGenerateTokenUnauthorized() throws Exception {
        when(userService.findUserByEmail("wrong.email")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/login")
                        .param("email", "wrong.email")
                        .param("password", "newPassword"))
                .andExpect(status().isUnauthorized());
    }
}