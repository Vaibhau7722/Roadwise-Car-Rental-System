package com.carrental.model;
import java.math.BigDecimal;
public record BookingReceipt(long id,String reference,int rentalDays,BigDecimal dailyRate,BigDecimal baseAmount) {}
