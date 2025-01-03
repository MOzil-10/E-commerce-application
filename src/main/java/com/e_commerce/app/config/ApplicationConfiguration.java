package com.e_commerce.app.config;

import com.e_commerce.app.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Defines a {@link UserDetailsService} bean that is used to load user-specific data during authentication.
     *
     * @return a {@link UserDetailsService} that retrieves user details from the database using the {@link UserRepository}.
     * @throws UsernameNotFoundException if the user is not found in the repository.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with the email " + username + " does not exist"));
    }

    /**
     * Defines a {@link BCryptPasswordEncoder} bean that is used for encoding passwords.
     *
     * @return a {@link BCryptPasswordEncoder} instance used to hash passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines an {@link AuthenticationManager} bean that manages the authentication process.
     *
     * @param config the {@link AuthenticationConfiguration} provided by Spring Security.
     * @return an {@link AuthenticationManager} instance used to handle authentication requests.
     * @throws Exception if an error occurs during the creation of the {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines an {@link AuthenticationProvider} bean that handles the authentication of user credentials.
     *
     * @return a {@link DaoAuthenticationProvider} configured with the custom {@link UserDetailsService} and {@link BCryptPasswordEncoder}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}