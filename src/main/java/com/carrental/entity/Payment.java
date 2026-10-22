package com.carrental.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="booking_id",nullable=false) private Booking booking;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(name="payment_status",nullable=false,length=20) private PaymentStatus paymentStatus=PaymentStatus.PENDING;
    @Column(name="paid_at") private LocalDateTime paidAt;
    @Column(nullable=false,length=30) private String method="SIMULATED";
    public enum PaymentStatus { PENDING, SIMULATED_PAID, REFUNDED }
    public Payment() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Booking getBooking(){return booking;} public void setBooking(Booking v){booking=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public PaymentStatus getPaymentStatus(){return paymentStatus;} public void setPaymentStatus(PaymentStatus v){paymentStatus=v;}
    public LocalDateTime getPaidAt(){return paidAt;} public void setPaidAt(LocalDateTime v){paidAt=v;}
    public String getMethod(){return method;} public void setMethod(String v){method=v;}
}
