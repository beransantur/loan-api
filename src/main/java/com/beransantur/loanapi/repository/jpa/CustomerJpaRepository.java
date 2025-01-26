package com.beransantur.loanapi.repository.jpa;

import com.beransantur.loanapi.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Integer> {

}
