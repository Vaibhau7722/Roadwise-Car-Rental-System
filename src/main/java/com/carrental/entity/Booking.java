package com.carrental.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="bookings")
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="booking_reference",nullable=false,unique=true,length=24) private String bookingReference;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="customer_id",nullable=false) private Customer customer;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="car_id",nullable=false) private Car car;
    @Column(name="pickup_date",nullable=false) private LocalDate pickupDate;
    @Column(name="return_date",nullable=false) private LocalDate returnDate;
    @Column(name="daily_rate",nullable=false,precision=10,scale=2) private BigDecimal dailyRate;
    @Column(name="rental_days",nullable=false) private int rentalDays;
    @Column(name="base_amount",nullable=false,precision=12,scale=2) private BigDecimal baseAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private BookingStatus status=BookingStatus.CONFIRMED;
    @Column(name="created_at",insertable=false,updatable=false) private LocalDateTime createdAt;
    @OneToOne(mappedBy="booking",fetch=FetchType.LAZY) private Rental rental;
    @OneToMany(mappedBy="booking",fetch=FetchType.LAZY) private java.util.List<Payment> payments=new java.util.ArrayList<>();
    public Booking() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getBookingReference(){return bookingReference;} public void setBookingReference(String v){bookingReference=v;}
    public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;}
    public Car getCar(){return car;} public void setCar(Car v){car=v;}
    public LocalDate getPickupDate(){return pickupDate;} public void setPickupDate(LocalDate v){pickupDate=v;}
    public LocalDate getReturnDate(){return returnDate;} public void setReturnDate(LocalDate v){returnDate=v;}
    public BigDecimal getDailyRate(){return dailyRate;} public void setDailyRate(BigDecimal v){dailyRate=v;}
    public int getRentalDays(){return rentalDays;} public void setRentalDays(int v){rentalDays=v;}
    public BigDecimal getBaseAmount(){return baseAmount;} public void setBaseAmount(BigDecimal v){baseAmount=v;}
    public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public Rental getRental(){return rental;} public java.util.List<Payment> getPayments(){return payments;}
}
