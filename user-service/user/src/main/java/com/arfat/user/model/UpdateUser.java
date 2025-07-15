package com.arfat.user.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateUser {
    private final String username;
    private final String password;

    private UpdateUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UpdateUser(Builder builder) {
        this(builder.username, builder.password);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String username;
        private String password;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UpdateUser build() {
            return new UpdateUser(this);
        }
    }
}
