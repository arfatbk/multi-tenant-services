package com.arfat.OAuth_server.security.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepositoty userRepository;

    public JpaUserDetailsService(UserRepositoty userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(this::getUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private User getUser(UserEntity user) {

        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getAuthorities().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorities
        );
    }
}
