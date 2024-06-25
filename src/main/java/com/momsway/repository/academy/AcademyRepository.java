package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import com.momsway.dto.AcademyDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcademyRepository extends JpaRepository<Academy,Long>, AcademyQueryDSL{
    @Query(" select new com.momsway.dto.AcademyDTO(a.aid, a.title, a.readNo, a.createAt, u.nickname) " +
            " from Academy a inner join a.academyUser u " +
            " where a.title like %:search_txt% " +
            " or a.content like %:search_txt% " +
            " order by a.aid desc ")
    List<AcademyDTO> findList(Pageable pageable, String search_txt);

    @Override
    long count();

    @Override
    void deleteById(Long aLong);
}
