package com.arfat.user;

import com.arfat.user.model.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
