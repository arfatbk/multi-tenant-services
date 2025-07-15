package com.arfat.user;

import com.arfat.user.model.UpdateUser;
import com.arfat.user.model.User;
import com.arfat.user.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultUserService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);
    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        this.userRepository.save(user);
        log.info("User created with username: {}", user.getUsername());
        return user;
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFound {
        log.info("Finding user with username: {}", username);
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound(String.format("User with username '%s' not found", username)));
    }


    @Override
    public User updateUser(String username, UpdateUser updateUser) throws UserNotFound {
        var userById = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound(String.format("User with username '%s' not found", username)));
        log.info("Updating user with username: {}", username);

        if (StringUtils.isNotEmpty(updateUser.getUsername())) {
            userById.setUsername(updateUser.getUsername());
            log.info("Updated username to: {}", updateUser.getUsername());
        }
        if (StringUtils.isNotEmpty(updateUser.getPassword())) {
            userById.setPassword(updateUser.getPassword());
            log.info("Updated password for username: {}", updateUser.getUsername());
        }
        this.userRepository.save(userById);
        return userById;
    }

    @Override
    public void deleteUser(String username) throws UserNotFound {
        var userById = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound(String.format("User with username '%s' not found", username)));
        log.info("Deleting user with username: {}", username);

        this.userRepository.deleteByUsername(userById.getUsername());
    }
}
