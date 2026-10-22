package com.carrental.model;

import java.io.Serializable;

public record UserSession(
        long userId,
        long customerId,
        String email,
        String fullName,
        String role
) implements Serializable {

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public long getUserId() {
        return userId;
    }

    public long getCustomerId() {
        return customerId;
    }
}