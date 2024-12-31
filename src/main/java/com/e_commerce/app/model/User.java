package com.e_commerce.app.model;

import com.e_commerce.app.enums.Role;
import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "customers")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;
    private String passwords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public String getPassword() {
        return passwords;  // Return the actual password field
    }

    @Override
    public String getUsername() {
        return username;  // Return the username field
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Assuming account is never expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Assuming account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Assuming credentials are never expired
    }

    @Override
    public boolean isEnabled() {
        return true;  // Assuming the user is always enabled
    }

    public User(){}

    public User(Long id, String username, String email, Role role, String address, String passwords) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.address = address;
        this.passwords = passwords;
    }
}
