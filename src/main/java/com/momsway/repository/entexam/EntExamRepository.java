package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntExamRepository extends JpaRepository<EntExam,Long>, EntExamQueryDSL {
    @Override
    void deleteById(Long aLong);

    @Override
    Optional<EntExam> findById(Long aLong);
}
