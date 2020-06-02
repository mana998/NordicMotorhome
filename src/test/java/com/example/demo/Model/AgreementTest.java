package com.example.demo.Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AgreementTest {

    @Test
    void getId() {
        Agreement agreement = new Agreement();
        agreement.setId(1);
        assertEquals(1, agreement.getId());
    }

    @Test
    void calculateExtraKmCost() {
        Agreement agreement = new Agreement();
        agreement.setDrivenKm(500);
        LocalDate date1 = LocalDate.of(2020, 5, 20);
        agreement.setStartDate(date1);
        LocalDate date2 = LocalDate.of(2020, 5, 27);
        agreement.setEndDate(date2);
        assertEquals(0, agreement.calculateExtraKmFee());
    }

    @Test
    void findDifferenceInDays() {
        Agreement agreement = new Agreement();
        LocalDate date1 = LocalDate.of(2020, 5, 20);
        LocalDate date2 = LocalDate.of(2020, 5, 21);
        assertEquals(1, agreement.findDifferenceInDays(date1, date2));
    }
}