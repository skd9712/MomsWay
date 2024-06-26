package com.momsway.repository.user;

import com.momsway.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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

//    @Query(" select count(u) " +
//            " from User u " +
//            " where :search like %:search_txt% ")
//    int getCount(String search, String search_txt);
//
//    @Query(" select u " +
//            " from User u " +
//            " where :search like %:search_txt% ")
//    List<User> findUsers(Pageable pageable, String search, String search_txt);

    @Query(" select u from User u where u.uid=:uid ")
    User getUserDetail(Long uid);

    @Query(" select u " +
            " from User u " +
            " where u.email like concat('%', :search_txt, '%') ")
    List<User> findUsersEmail(Pageable pageable, String search_txt);

    @Query(" select u " +
            " from User u " +
            " where u.nickname like concat('%', :search_txt, '%') ")
    List<User> findUsersNick(Pageable pageable, String search_txt);

    @Override
    void deleteById(Long uid);
}
