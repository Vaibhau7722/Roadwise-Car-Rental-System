package com.carrental.service;

import java.time.LocalDate;

public final class BookingRules {
    private BookingRules() {}
    public static boolean canCancel(String status,LocalDate pickup,LocalDate today){return "CONFIRMED".equals(status)&&pickup!=null&&pickup.isAfter(today);}
    public static boolean canBook(String carStatus,boolean hasOverlappingReservation){return "AVAILABLE".equals(carStatus)&&!hasOverlappingReservation;}
}
