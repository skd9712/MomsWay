package com.momsway.repository.like;


import com.momsway.domain.EntLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<EntLike,Long>, LikeQueryDSL {

    @Override
    <S extends EntLike> S save(S entity);

    @Override
    void deleteById(Long aLong);

}
