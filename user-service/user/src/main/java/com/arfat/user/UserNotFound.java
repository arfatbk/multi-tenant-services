package com.arfat.user;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }
}
