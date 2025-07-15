package com.arfat.orchastrator.persistence.entity;

import com.arfat.base.multitenancy.jpa.BaseEntity;
import com.arfat.user.model.User;
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

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEnabled(user.isEnabled());
        userEntity.setAccountNonExpired(user.isAccountNonExpired());
        userEntity.setCredentialsNonExpired(user.isCredentialsNonExpired());
        userEntity.setAccountNonLocked(user.isAccountNonLocked());
        userEntity.setAuthorities(user.getAuthorities());
        return userEntity;
    }

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

    public User toModel() {
        User user = new User(this.username, this.password);
        user.setEnabled(this.enabled);
        user.setAccountNonExpired(this.accountNonExpired);
        user.setCredentialsNonExpired(this.credentialsNonExpired);
        user.setAccountNonLocked(this.accountNonLocked);
        user.setAuthorities(this.authorities);

        return user;
    }
}
