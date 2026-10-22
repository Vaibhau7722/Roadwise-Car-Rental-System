package com.carrental.service;

import com.carrental.exception.InvalidBookingDatesException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalPricingService {
    public static final BigDecimal LATE_FEE_PER_DAY=new BigDecimal("25.00");
    public int rentalDays(LocalDate pickup,LocalDate returned){if(pickup==null||returned==null||!returned.isAfter(pickup))throw new InvalidBookingDatesException("Return date must be after pickup date.");return Math.toIntExact(ChronoUnit.DAYS.between(pickup,returned));}
    public void validateBookingDates(LocalDate pickup,LocalDate returned,LocalDate today){if(pickup==null||returned==null)throw new InvalidBookingDatesException("Choose both pickup and return dates.");if(pickup.isBefore(today))throw new InvalidBookingDatesException("Pickup date cannot be in the past.");rentalDays(pickup,returned);}
    public BigDecimal rentalCost(BigDecimal dailyRate,int days){if(dailyRate==null||dailyRate.signum()<0||days<1)throw new IllegalArgumentException("A non-negative rate and positive rental duration are required.");return dailyRate.multiply(BigDecimal.valueOf(days)).setScale(2,RoundingMode.HALF_UP);}
    public int lateDays(LocalDate plannedReturn,LocalDate actualReturn){return Math.max(0,Math.toIntExact(ChronoUnit.DAYS.between(plannedReturn,actualReturn)));}
    public BigDecimal lateFee(int days){if(days<0)throw new IllegalArgumentException("Late days cannot be negative.");return LATE_FEE_PER_DAY.multiply(BigDecimal.valueOf(days)).setScale(2,RoundingMode.HALF_UP);}
    public BigDecimal finalCharge(BigDecimal base,BigDecimal lateFee){return base.add(lateFee).setScale(2,RoundingMode.HALF_UP);}
}
