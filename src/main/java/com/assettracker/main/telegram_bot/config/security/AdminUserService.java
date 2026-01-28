package com.assettracker.main.telegram_bot.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminUserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private static final String USERNAME = "admin";
    private final String password;

    public AdminUserService(
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_PASSWORD}") String password
    ) {
        this.passwordEncoder = passwordEncoder;
        this.password = password;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(USERNAME)) {
            return new User(username, passwordEncoder.encode(password),
                    Set.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())));
        } else {
            return null;
        }
    }
}
