package com.arfat.orchastrator.config;

import com.arfat.orchastrator.persistence.impl.DefaultUserRepository;
import com.arfat.user.DefaultUserService;
import com.arfat.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {


    @Bean
    UserService userService(DefaultUserRepository userRepositoryْ) {
        return new DefaultUserService(userRepositoryْ);
    }
}
