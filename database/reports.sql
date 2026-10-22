USE car_rental_db;

-- Monthly rental revenue based on completed rentals and simulated paid payments.
SELECT DATE_FORMAT(p.paid_at,'%Y-%m') AS revenue_month, SUM(p.amount) AS revenue
FROM payments p JOIN bookings b ON b.id=p.booking_id
WHERE p.payment_status='SIMULATED_PAID' GROUP BY DATE_FORMAT(p.paid_at,'%Y-%m') ORDER BY revenue_month;

-- Bookings by status and month.
SELECT DATE_FORMAT(created_at,'%Y-%m') AS booking_month,status,COUNT(*) AS booking_count
FROM bookings GROUP BY DATE_FORMAT(created_at,'%Y-%m'),status ORDER BY booking_month,status;

-- Most frequently rented cars, excluding cancelled reservations.
SELECT c.id,c.brand,c.model,c.registration_number,COUNT(b.id) AS rental_count
FROM cars c LEFT JOIN bookings b ON b.car_id=c.id AND b.status IN ('ACTIVE','COMPLETED')
GROUP BY c.id,c.brand,c.model,c.registration_number ORDER BY rental_count DESC,c.brand,c.model;

-- Monthly billed rental revenue from completed rental records.
SELECT DATE_FORMAT(r.actual_return_at,'%Y-%m') AS revenue_month,SUM(r.final_charge) AS billed_amount
FROM rentals r WHERE r.actual_return_at IS NOT NULL GROUP BY DATE_FORMAT(r.actual_return_at,'%Y-%m') ORDER BY revenue_month;
