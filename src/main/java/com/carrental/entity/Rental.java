package com.carrental.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="rentals")
public class Rental {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="booking_id",nullable=false,unique=true) private Booking booking;
    @Column(name="pickup_at") private LocalDateTime pickupAt;
    @Column(name="actual_return_at") private LocalDateTime actualReturnAt;
    @Column(name="late_days",nullable=false) private int lateDays;
    @Column(name="late_fee_per_day",nullable=false,precision=10,scale=2) private BigDecimal lateFeePerDay;
    @Column(name="late_fee",nullable=false,precision=12,scale=2) private BigDecimal lateFee;
    @Column(name="final_charge",nullable=false,precision=12,scale=2) private BigDecimal finalCharge;
    public Rental() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Booking getBooking(){return booking;} public void setBooking(Booking v){booking=v;}
    public LocalDateTime getPickupAt(){return pickupAt;} public void setPickupAt(LocalDateTime v){pickupAt=v;}
    public LocalDateTime getActualReturnAt(){return actualReturnAt;} public void setActualReturnAt(LocalDateTime v){actualReturnAt=v;}
    public int getLateDays(){return lateDays;} public void setLateDays(int v){lateDays=v;}
    public BigDecimal getLateFeePerDay(){return lateFeePerDay;} public void setLateFeePerDay(BigDecimal v){lateFeePerDay=v;}
    public BigDecimal getLateFee(){return lateFee;} public void setLateFee(BigDecimal v){lateFee=v;}
    public BigDecimal getFinalCharge(){return finalCharge;} public void setFinalCharge(BigDecimal v){finalCharge=v;}
}
