package com.momsway.repository.user;

import com.momsway.domain.User;
import com.momsway.domain.UserRole;
import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.momsway.domain.QEntExam.*;
import static com.momsway.domain.QAcademy.*;
import static com.momsway.domain.QUser.user;

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
    public List<AcademyDTO> myacalist(long uidByEmail) {
        List<AcademyDTO> list = queryFactory.select(Projections.fields(AcademyDTO.class, academy.title, academy.aid))
                .from(academy)
                .where(academy.academyUser.uid.eq(uidByEmail))
                .fetch();
        return list;
    }
    @Override
    public int getCount(String search, String search_txt) {

        int totalCount = 0;

        if (search == "email" || "email".equals(search)) {
            Long count = queryFactory.select(user.uid.count())
                    .from(user)
                    .where(user.email.like("%" + search_txt + "%").and(user.pwd.isNotNull())
                            .and(user.role.ne(UserRole.ADMIN)))
                    .fetchOne();
            totalCount = count.intValue();
        } else if (search == "nickname" || "nickname".equals(search)) {
            Long count = queryFactory.select(user.uid.count())
                    .from(user)
                    .where(user.nickname.like("%" + search_txt + "%").and(user.pwd.isNotNull())
                            .and(user.role.ne(UserRole.ADMIN)))
                    .fetchOne();
            totalCount = count.intValue();
        } else {
            Long count = queryFactory.select(user.uid.count())
                    .from(user)
                    .where(user.pwd.isNotNull().and(user.role.ne(UserRole.ADMIN)))
                    .fetchOne();
            totalCount = count.intValue();
        }
        return totalCount;
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
