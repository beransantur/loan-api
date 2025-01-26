package com.beransantur.loanapi.repository.jpa;

import com.beransantur.loanapi.repository.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, Integer> {

}
