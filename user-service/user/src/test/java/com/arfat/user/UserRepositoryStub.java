package com.arfat.user;

import com.arfat.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryStub implements UserRepository {
    private final Map<String, User> userStore = new HashMap<>();

    @Override
    public void save(User user) {
        this.userStore.putIfAbsent(user.getUsername(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(this.userStore.get(username));
    }

    @Override
    public void deleteByUsername(String username) {
        this.userStore.remove(username);
    }
}
