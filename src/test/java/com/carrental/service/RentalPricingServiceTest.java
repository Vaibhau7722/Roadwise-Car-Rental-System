package com.carrental.service;

import com.carrental.exception.InvalidBookingDatesException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class RentalPricingServiceTest {
    private final RentalPricingService pricing=new RentalPricingService();
    @Test void october20ThroughOctober23IsThreeDays(){assertEquals(3,pricing.rentalDays(LocalDate.parse("2026-10-20"),LocalDate.parse("2026-10-23")));}
    @Test void costUsesBigDecimalDailyRateTimesDays(){assertEquals(new BigDecimal("162.00"),pricing.rentalCost(new BigDecimal("54.00"),3));}
    @Test void lateFeeUsesConfiguredDailyFee(){assertEquals(new BigDecimal("50.00"),pricing.lateFee(2));assertEquals(new BigDecimal("0.00"),pricing.lateFee(0));}
    @Test void pickupCannotBeBeforeToday(){assertThrows(InvalidBookingDatesException.class,()->pricing.validateBookingDates(LocalDate.parse("2026-10-17"),LocalDate.parse("2026-10-20"),LocalDate.parse("2026-10-18")));}
    @Test void returnMustBeAfterPickup(){assertThrows(InvalidBookingDatesException.class,()->pricing.rentalDays(LocalDate.parse("2026-10-20"),LocalDate.parse("2026-10-20")));}
    @Test void finalChargeAddsBaseAndLateFee(){assertEquals(new BigDecimal("187.00"),pricing.finalCharge(new BigDecimal("162.00"),new BigDecimal("25.00")));}
}
