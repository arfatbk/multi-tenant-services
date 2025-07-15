package com.arfat.orchastrator.user;

import com.arfat.user.model.UpdateUser;
import com.arfat.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserRequest {

    private String username;
    private String password;

    public UserRequest() {
    }

    public User toUser() {
        return new User(this.username, this.password);
    }

    public UpdateUser toUpdateUser() {
        return UpdateUser.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
