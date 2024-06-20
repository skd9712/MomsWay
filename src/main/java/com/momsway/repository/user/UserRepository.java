package com.momsway.repository.user;

import com.momsway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, UserQueryDSL {
    @Override
    Optional<User> findById(Long aLong);
}
