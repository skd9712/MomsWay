package com.momsway.repository.user;

import com.momsway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, UserQueryDSL {
    @Override
    Optional<User> findById(Long aLong);

    @Override
    <S extends User> S save(S entity);

    @Query(" select u from User u where u.email=:email ")
    User findByEmail(String email);

    @Query(" select u from User u where u.nickname=:nickname ")
    User findByNickname(String nickname);
}
