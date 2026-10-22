package com.carrental.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="maintenance")
public class Maintenance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="car_id",nullable=false) private Car car;
    @Column(nullable=false,length=255) private String description;
    @Column(name="start_date",nullable=false) private LocalDate startDate;
    @Column(name="end_date") private LocalDate endDate;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal cost=BigDecimal.ZERO;
    public Maintenance() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Car getCar(){return car;} public void setCar(Car v){car=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getStartDate(){return startDate;} public void setStartDate(LocalDate v){startDate=v;}
    public LocalDate getEndDate(){return endDate;} public void setEndDate(LocalDate v){endDate=v;}
    public BigDecimal getCost(){return cost;} public void setCost(BigDecimal v){cost=v;}
}
