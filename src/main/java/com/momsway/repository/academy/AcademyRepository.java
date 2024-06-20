package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademyRepository extends JpaRepository<Academy,Long>, AcademyQueryDSL{
}
