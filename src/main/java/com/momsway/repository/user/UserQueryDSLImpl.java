package com.momsway.repository.user;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.momsway.domain.QEntExam.*;
import static com.momsway.domain.QUser.*;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQueryDSLImpl implements UserQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EntExamDTO> myentlist(long uidByEmail) {
        List<EntExamDTO> list = queryFactory.select(Projections.fields(EntExamDTO.class, entExam.title, entExam.eid))
                .from(entExam)
                .where(entExam.entExamUser.uid.eq(uidByEmail))
                .fetch();
        return list;
    }

    @Override
    public UserDTO findByUserInfo(long uidByEmail) {
        UserDTO dto = queryFactory.select(Projections.fields(UserDTO.class
                        , user.uid
                        , user.email
                        , user.nickname))
                .from(user)
                .where(user.uid.eq(uidByEmail))
                .fetchOne();
        return dto;
    }
}
