package com.beransantur.loanapi.repository;

import com.beransantur.loanapi.repository.entity.CustomerEntity;
import com.beransantur.loanapi.repository.jpa.CustomerJpaRepository;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;

    public CustomerEntity getById(Integer id) {
        return customerJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("customer", id));

    }
}
