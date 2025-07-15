package com.arfat.orchastrator.user;

import com.arfat.user.model.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {
    private final String username;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final String authorities;


    public UserResponse(User user) {
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.authorities = user.getAuthorities();
    }

    public static UserResponse from(User user) {
        return new UserResponse(user);
    }
}
