package com.beransantur.loanapi.repository;

import com.beransantur.loanapi.repository.entity.LoanEntity;
import com.beransantur.loanapi.repository.jpa.LoanJpaRepository;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoanRepository {
    private final LoanJpaRepository loanJpaRepository;

    public Loan getById(Integer id) {
        return loanJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("loan", id))
                .toModel();

    }
}
