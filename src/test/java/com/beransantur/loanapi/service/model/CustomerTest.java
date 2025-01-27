package com.beransantur.loanapi.service.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldReturnTrue_whenHasEnoughLimit() {
        // given
        Customer customer = Customer.builder()
                .creditLimit(new BigDecimal("10000"))
                .usedCreditLimit(new BigDecimal("2000"))
                .build();
        BigDecimal requestedAmount = new BigDecimal("5000");

        // when
        boolean result = customer.hasEnoughLimit(requestedAmount);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_whenDoesNotHaveEnoughLimit() {
        // given
        Customer customer = Customer.builder()
                .creditLimit(new BigDecimal("10000"))
                .usedCreditLimit(new BigDecimal("9000"))
                .build();
        BigDecimal requestedAmount = new BigDecimal("2000");

        // when
        boolean result = customer.hasEnoughLimit(requestedAmount);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReduceCreditLimit_whenCalledWithValidAmount() {
        // given
        Customer customer = Customer.builder()
                .creditLimit(new BigDecimal("10000"))
                .usedCreditLimit(new BigDecimal("2000"))
                .build();
        BigDecimal reduceAmount = new BigDecimal("3000");

        // when
        customer.reduceCreditLimit(reduceAmount);

        // then
        assertEquals(new BigDecimal("7000"), customer.getCreditLimit());
        assertEquals(new BigDecimal("5000"), customer.getUsedCreditLimit());
    }

    @Test
    void shouldHandleZeroReduction_whenCalledWithZeroAmount() {
        // given
        Customer customer = Customer.builder()
                .creditLimit(new BigDecimal("10000"))
                .usedCreditLimit(new BigDecimal("2000"))
                .build();
        BigDecimal reduceAmount = BigDecimal.ZERO;

        // when
        customer.reduceCreditLimit(reduceAmount);

        // then
        assertEquals(new BigDecimal("10000"), customer.getCreditLimit());
        assertEquals(new BigDecimal("2000"), customer.getUsedCreditLimit());
    }

}







