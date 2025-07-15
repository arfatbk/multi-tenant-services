package com.arfat.OAuth_server.security.user;

import com.arfat.base.multitenancy.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "enabled", nullable = false)
    @NotNull
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    @NotNull
    private boolean accountNonExpired;

    @Column(name = "credentials_non_expired", nullable = false)
    @NotNull
    private boolean credentialsNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    @NotNull
    private boolean accountNonLocked;

    private String authorities;

    @Override
    public String toString() {
        return "UserEntity {" +
               "username='" + username +
               ", password='[PROTECTED]'" +
               ", enabled=" + enabled +
               ", accountNonExpired=" + accountNonExpired +
               ", credentialsNonExpired=" + credentialsNonExpired +
               ", accountNonLocked=" + accountNonLocked +
               ", authorities='" + authorities +
               '}';
    }
}
