package com.coffee.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();

    public CustomUserDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${spring.security.user.name}") String adminUsername,
            @Value("${spring.security.user.password}") String adminPassword) {
        
        // Create admin user with ADMIN role
        users.put(adminUsername, User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build());

        // Create a default user with USER role
        users.put("user", User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build());

        // Create a default staff with STAFF role
        users.put("staff", User.builder()
                .username("staff")
                .password(passwordEncoder.encode("staff123"))
                .roles("STAFF")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return users.get(username);
    }
} 