package com.beransantur.loanapi.service;

import com.beransantur.loanapi.repository.CustomerRepository;
import com.beransantur.loanapi.repository.entity.CustomerEntity;
import com.beransantur.loanapi.service.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Integer id) {
        CustomerEntity customerEntity = customerRepository.getById(id);
        return customerEntity.toModel();
    }
}
