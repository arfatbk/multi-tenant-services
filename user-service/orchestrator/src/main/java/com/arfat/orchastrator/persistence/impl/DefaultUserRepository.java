package com.arfat.orchastrator.persistence.impl;

import com.arfat.orchastrator.persistence.entity.UserEntity;
import com.arfat.user.UserRepository;
import com.arfat.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public DefaultUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(User user) {
        this.jpaUserRepository.save(UserEntity.from(user));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.jpaUserRepository.findByUsername(username)
                .map(UserEntity::toModel);
    }

    @Override
    public void deleteByUsername(String username) {
        this.jpaUserRepository.findByUsername(username)
                .ifPresent(this.jpaUserRepository::delete);
    }
}


