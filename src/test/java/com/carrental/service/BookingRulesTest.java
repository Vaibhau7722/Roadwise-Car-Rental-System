package com.carrental.service;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookingRulesTest {
    private final LocalDate today=LocalDate.parse("2026-10-18");
    @Test void onlyFutureConfirmedBookingsCanBeCancelled(){assertTrue(BookingRules.canCancel("CONFIRMED",today.plusDays(1),today));assertFalse(BookingRules.canCancel("ACTIVE",today.plusDays(1),today));assertFalse(BookingRules.canCancel("CANCELLED",today.plusDays(1),today));assertFalse(BookingRules.canCancel("CONFIRMED",today,today));}
    @Test void bookableCarMustBeGenerallyAvailableAndHaveNoDateOverlap(){assertTrue(BookingRules.canBook("AVAILABLE",false));assertFalse(BookingRules.canBook("AVAILABLE",true));assertFalse(BookingRules.canBook("MAINTENANCE",false));assertFalse(BookingRules.canBook("INACTIVE",false));}
}
