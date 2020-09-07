package com.learn.spring.springbootauthenticationwithjwt.repository;

import com.learn.spring.springbootauthenticationwithjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
