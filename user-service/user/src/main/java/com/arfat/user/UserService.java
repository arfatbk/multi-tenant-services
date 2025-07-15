package com.arfat.user;
 import com.arfat.user.model.UpdateUser;
 import com.arfat.user.model.User;

public interface UserService {
    User createUser(User user);

    User findUserByUsername(String username) throws UserNotFound;

    User updateUser(String username, UpdateUser updateUser) throws UserNotFound;

    void deleteUser(String username) throws UserNotFound;
}
