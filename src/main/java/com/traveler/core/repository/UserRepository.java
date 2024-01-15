package com.traveler.core.repository;

import com.traveler.core.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    boolean existsUserByNickname(String nickname);
}
