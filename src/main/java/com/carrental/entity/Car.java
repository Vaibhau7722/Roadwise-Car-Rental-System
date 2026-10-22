package com.carrental.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="cars")
public class Car {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=60) private String brand;
    @Column(nullable=false,length=80) private String model;
    @Column(name="registration_number",nullable=false,unique=true,length=25) private String registrationNumber;
    @Column(nullable=false,length=40) private String category;
    @Column(name="fuel_type",nullable=false,length=30) private String fuelType;
    @Column(nullable=false,length=30) private String transmission;
    @Column(name="seating_capacity",nullable=false) private int seatingCapacity;
    @Column(name="daily_rate",nullable=false,precision=10,scale=2) private BigDecimal dailyRate;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private CarStatus status=CarStatus.AVAILABLE;
    @Column(name="image_url",length=500) private String imageUrl;
    @Column(name="created_at",insertable=false,updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="car",fetch=FetchType.LAZY) private List<Booking> bookings=new ArrayList<>();
    @OneToMany(mappedBy="car",fetch=FetchType.LAZY) private List<Maintenance> maintenanceRecords=new ArrayList<>();
    public Car() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getModel(){return model;} public void setModel(String v){model=v;}
    public String getRegistrationNumber(){return registrationNumber;} public void setRegistrationNumber(String v){registrationNumber=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getFuelType(){return fuelType;} public void setFuelType(String v){fuelType=v;}
    public String getTransmission(){return transmission;} public void setTransmission(String v){transmission=v;}
    public int getSeatingCapacity(){return seatingCapacity;} public void setSeatingCapacity(int v){seatingCapacity=v;}
    public BigDecimal getDailyRate(){return dailyRate;} public void setDailyRate(BigDecimal v){dailyRate=v;}
    public CarStatus getStatus(){return status;} public void setStatus(CarStatus v){status=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public List<Booking> getBookings(){return bookings;} public List<Maintenance> getMaintenanceRecords(){return maintenanceRecords;}
}
