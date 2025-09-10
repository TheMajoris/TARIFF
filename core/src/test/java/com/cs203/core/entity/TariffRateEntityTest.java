package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TariffRateEntity Tests")
class TariffRateEntityTest {

    @Test
    @DisplayName("Should handle helper methods correctly")
    void shouldHandleHelperMethodsCorrectly() {
        LocalDate now = LocalDate.now();
        
        // Test currently active rate
        TariffRateEntity activeRate = new TariffRateEntity();
        activeRate.setEffectiveDate(now.minusDays(30)); // Started 30 days ago
        activeRate.setExpiryDate(now.plusDays(30)); // Expires in 30 days
        
        assertTrue(activeRate.isCurrentlyActive());
        assertTrue(activeRate.isEffective());
        assertFalse(activeRate.isExpired());
        
        // Test expired rate
        TariffRateEntity expiredRate = new TariffRateEntity();
        expiredRate.setEffectiveDate(now.minusDays(60));
        expiredRate.setExpiryDate(now.minusDays(30)); // Expired 30 days ago
        
        assertFalse(expiredRate.isCurrentlyActive());
        assertTrue(expiredRate.isEffective());
        assertTrue(expiredRate.isExpired());
        
        // Test future rate
        TariffRateEntity futureRate = new TariffRateEntity();
        futureRate.setEffectiveDate(now.plusDays(30)); // Starts in 30 days
        futureRate.setExpiryDate(now.plusDays(60));
        
        assertFalse(futureRate.isCurrentlyActive());
        assertFalse(futureRate.isEffective());
        assertFalse(futureRate.isExpired());
        
        // Test permanent rate (no expiry)
        TariffRateEntity permanentRate = new TariffRateEntity();
        permanentRate.setEffectiveDate(now.minusDays(30));
        permanentRate.setExpiryDate(null); // No expiry date
        
        assertTrue(permanentRate.isCurrentlyActive());
        assertTrue(permanentRate.isEffective());
        assertFalse(permanentRate.isExpired());
    }
}
