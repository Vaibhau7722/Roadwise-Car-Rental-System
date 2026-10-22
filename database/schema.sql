CREATE DATABASE IF NOT EXISTS car_rental_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE car_rental_db;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role ENUM('ADMIN','CUSTOMER') NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_users_role_active(role, active)
) ENGINE=InnoDB;

CREATE TABLE customers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  full_name VARCHAR(120) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_customers_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE cars (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  brand VARCHAR(60) NOT NULL,
  model VARCHAR(80) NOT NULL,
  registration_number VARCHAR(25) NOT NULL UNIQUE,
  category VARCHAR(40) NOT NULL,
  fuel_type VARCHAR(30) NOT NULL,
  transmission VARCHAR(30) NOT NULL,
  seating_capacity TINYINT UNSIGNED NOT NULL,
  daily_rate DECIMAL(10,2) NOT NULL,
  status ENUM('AVAILABLE','UNAVAILABLE','MAINTENANCE','INACTIVE') NOT NULL DEFAULT 'AVAILABLE',
  image_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_cars_status_category(status, category),
  INDEX idx_cars_brand_model(brand, model),
  CONSTRAINT chk_cars_capacity CHECK(seating_capacity BETWEEN 1 AND 20),
  CONSTRAINT chk_cars_rate CHECK(daily_rate >= 0)
) ENGINE=InnoDB;

CREATE TABLE bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_reference VARCHAR(24) NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL,
  car_id BIGINT NOT NULL,
  pickup_date DATE NOT NULL,
  return_date DATE NOT NULL,
  daily_rate DECIMAL(10,2) NOT NULL,
  rental_days INT NOT NULL,
  base_amount DECIMAL(12,2) NOT NULL,
  status ENUM('CONFIRMED','ACTIVE','COMPLETED','CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bookings_customer FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
  CONSTRAINT fk_bookings_car FOREIGN KEY(car_id) REFERENCES cars(id) ON DELETE RESTRICT,
  CONSTRAINT chk_booking_dates CHECK(return_date > pickup_date),
  CONSTRAINT chk_booking_days CHECK(rental_days > 0),
  INDEX idx_bookings_car_dates_status(car_id, status, pickup_date, return_date),
  INDEX idx_bookings_customer_created(customer_id, created_at),
  INDEX idx_bookings_status(status)
) ENGINE=InnoDB;

CREATE TABLE rentals (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_id BIGINT NOT NULL UNIQUE,
  pickup_at DATETIME,
  actual_return_at DATETIME,
  late_days INT NOT NULL DEFAULT 0,
  late_fee_per_day DECIMAL(10,2) NOT NULL DEFAULT 25.00,
  late_fee DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  final_charge DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  CONSTRAINT fk_rentals_booking FOREIGN KEY(booking_id) REFERENCES bookings(id) ON DELETE RESTRICT,
  INDEX idx_rentals_return(actual_return_at)
) ENGINE=InnoDB;

CREATE TABLE payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  payment_status ENUM('PENDING','SIMULATED_PAID','REFUNDED') NOT NULL DEFAULT 'PENDING',
  paid_at DATETIME,
  method VARCHAR(30) NOT NULL DEFAULT 'SIMULATED',
  CONSTRAINT fk_payments_booking FOREIGN KEY(booking_id) REFERENCES bookings(id) ON DELETE RESTRICT,
  INDEX idx_payments_booking_status(booking_id, payment_status)
) ENGINE=InnoDB;

CREATE TABLE maintenance (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  car_id BIGINT NOT NULL,
  description VARCHAR(255) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  CONSTRAINT fk_maintenance_car FOREIGN KEY(car_id) REFERENCES cars(id) ON DELETE RESTRICT,
  CONSTRAINT chk_maintenance_dates CHECK(end_date IS NULL OR end_date >= start_date),
  INDEX idx_maintenance_car_dates(car_id, start_date, end_date)
) ENGINE=InnoDB;
