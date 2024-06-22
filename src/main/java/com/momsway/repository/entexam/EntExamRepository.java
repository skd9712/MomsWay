package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntExamRepository extends JpaRepository<EntExam,Long>, EntExamQueryDSL {
    @Override
    <S extends EntExam> S save(S entity);

    @Override
    List<EntExam> findAll();

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<EntExam> findById(Long aLong);

    @Transactional
    @Modifying
    @Query(" update EntExam e set e.readNo = e.readNo+1 where e.eid = :eid")
    void incrementReadNo(Long eid);
}
