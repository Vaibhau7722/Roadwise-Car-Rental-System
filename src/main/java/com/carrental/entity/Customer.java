package com.carrental.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="customers")
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false, unique=true) private User user;
    @Column(name="full_name", nullable=false, length=120) private String fullName;
    @Column(nullable=false, length=30) private String phone;
    @Column(name="created_at", insertable=false, updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="customer", fetch=FetchType.LAZY) private List<Booking> bookings=new ArrayList<>();
    public Customer() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public User getUser(){return user;} public void setUser(User v){user=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public List<Booking> getBookings(){return bookings;}
}
