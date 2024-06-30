package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import com.momsway.dto.AcademyDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademyRepository extends JpaRepository<Academy,Long>, AcademyQueryDSL{
    @Query(" select new com.momsway.dto.AcademyDTO(a.aid, a.title, a.readNo, a.createAt, u.nickname) " +
            " from Academy a inner join a.academyUser u " +
            " where a.title like %:search_txt% " +
            " or a.content like %:search_txt% " +
            " order by a.aid desc ")
    List<AcademyDTO> findList(Pageable pageable, String search_txt);

    @Query(" select count(a.aid) " +
            " from Academy a " +
            " where a.title like %:search_txt% " +
            " or a.content like %:search_txt% " +
            " order by a.aid desc ")
    long count(String search_txt);

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<Academy> findById(Long aLong);
}
