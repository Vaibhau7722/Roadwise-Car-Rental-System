package com.carrental.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=190) private String email;
    @Column(name="password_hash", nullable=false, length=100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=10) private Role role;
    @Column(nullable=false) private boolean active=true;
    @Column(name="created_at", insertable=false, updatable=false) private LocalDateTime createdAt;
    @OneToOne(mappedBy="user", fetch=FetchType.LAZY) private Customer customer;
    public User() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;}
}
