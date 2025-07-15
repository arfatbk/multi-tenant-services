package com.arfat.orchastrator.persistence.impl;

import com.arfat.orchastrator.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);
}


