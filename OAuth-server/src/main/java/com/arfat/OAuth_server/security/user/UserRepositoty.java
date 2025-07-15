package com.arfat.OAuth_server.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoty extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);
}
