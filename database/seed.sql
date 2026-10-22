USE car_rental_db;

-- Demo-only hash for the password "password". Change it before exposing a deployment.
INSERT INTO users(id,email,password_hash,role) VALUES
(1,'admin@carrental.local','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','ADMIN'),
(2,'alice@example.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER'),
(3,'ben@example.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER'),
(4,'chloe@example.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER'),
(5,'dev@example.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER'),
(6,'emil@example.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER');
INSERT INTO customers(id,user_id,full_name,phone) VALUES
(1,2,'Alice Morgan','555-0101'),(2,3,'Ben Carter','555-0102'),(3,4,'Chloe Singh','555-0103'),
(4,5,'Dev Patel','555-0104'),(5,6,'Emil Reyes','555-0105');

INSERT INTO cars(id,brand,model,registration_number,category,fuel_type,transmission,seating_capacity,daily_rate,status,image_url) VALUES
(1,'Toyota','Corolla','CR-1001','Sedan','Petrol','Automatic',5,54.00,'AVAILABLE',''),
(2,'Honda','Civic','CR-1002','Sedan','Petrol','Automatic',5,59.00,'AVAILABLE',''),
(3,'Hyundai','Creta','CR-1003','SUV','Petrol','Automatic',5,72.00,'AVAILABLE',''),
(4,'Kia','Seltos','CR-1004','SUV','Diesel','Automatic',5,75.00,'AVAILABLE',''),
(5,'Maruti','Swift','CR-1005','Hatchback','Petrol','Manual',5,38.00,'AVAILABLE',''),
(6,'Tata','Nexon','CR-1006','SUV','Electric','Automatic',5,68.00,'AVAILABLE',''),
(7,'Mahindra','Thar','CR-1007','SUV','Diesel','Manual',4,88.00,'MAINTENANCE',''),
(8,'Volkswagen','Virtus','CR-1008','Sedan','Petrol','Automatic',5,64.00,'AVAILABLE',''),
(9,'Renault','Kwid','CR-1009','Hatchback','Petrol','Manual',5,32.00,'UNAVAILABLE',''),
(10,'Toyota','Innova','CR-1010','MPV','Diesel','Automatic',7,95.00,'AVAILABLE','');

INSERT INTO bookings(id,booking_reference,customer_id,car_id,pickup_date,return_date,daily_rate,rental_days,base_amount,status) VALUES
(1,'CR-2026-000001',1,1,'2026-09-05','2026-09-08',54.00,3,162.00,'COMPLETED'),
(2,'CR-2026-000002',2,3,'2026-10-20','2026-10-23',72.00,3,216.00,'CONFIRMED'),
(3,'CR-2026-000003',3,2,'2026-10-22','2026-10-25',59.00,3,177.00,'CONFIRMED'),
(4,'CR-2026-000004',4,5,'2026-08-10','2026-08-12',38.00,2,76.00,'COMPLETED'),
(5,'CR-2026-000005',5,8,'2026-11-02','2026-11-06',64.00,4,256.00,'CONFIRMED'),
(6,'CR-2026-000006',1,4,'2026-07-14','2026-07-16',75.00,2,150.00,'CANCELLED');
INSERT INTO rentals(booking_id,pickup_at,actual_return_at,late_days,late_fee_per_day,late_fee,final_charge) VALUES
(1,'2026-09-05 10:00:00','2026-09-08 11:00:00',0,25.00,0.00,162.00),
(4,'2026-08-10 09:00:00','2026-08-13 13:00:00',1,25.00,25.00,101.00);
INSERT INTO payments(booking_id,amount,payment_status,paid_at,method) VALUES
(1,162.00,'SIMULATED_PAID','2026-09-08 11:00:00','SIMULATED'),
(4,101.00,'SIMULATED_PAID','2026-08-13 13:00:00','SIMULATED'),
(2,216.00,'PENDING',NULL,'SIMULATED');
INSERT INTO maintenance(car_id,description,start_date,end_date,cost) VALUES
(7,'Scheduled brake and tire service','2026-10-10',NULL,350.00);
