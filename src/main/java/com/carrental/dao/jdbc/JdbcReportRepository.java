package com.carrental.dao.jdbc;

import com.carrental.util.Database;
import java.sql.*;
import java.util.*;

public class JdbcReportRepository {
    public List<Map<String,Object>> monthlyRevenue()throws SQLException {String sql="SELECT DATE_FORMAT(p.paid_at,'%Y-%m') month,SUM(p.amount) revenue,COUNT(*) payments FROM payments p WHERE p.payment_status='SIMULATED_PAID' GROUP BY DATE_FORMAT(p.paid_at,'%Y-%m') ORDER BY month DESC";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){List<Map<String,Object>>a=new ArrayList<>();while(r.next())a.add(Map.of("month",r.getString("month"),"revenue",r.getBigDecimal("revenue"),"payments",r.getLong("payments")));return a;}}
    public List<Map<String,Object>> mostRented()throws SQLException {String sql="SELECT ca.brand,ca.model,ca.registration_number,COUNT(b.id) rentals,COALESCE(SUM(b.rental_days),0) rental_days FROM cars ca LEFT JOIN bookings b ON b.car_id=ca.id AND b.status IN ('ACTIVE','COMPLETED') GROUP BY ca.id,ca.brand,ca.model,ca.registration_number ORDER BY rentals DESC,ca.brand,ca.model";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){List<Map<String,Object>>a=new ArrayList<>();while(r.next())a.add(Map.of("brand",r.getString("brand"),"model",r.getString("model"),"registrationNumber",r.getString("registration_number"),"rentals",r.getLong("rentals"),"rentalDays",r.getLong("rental_days")));return a;}}
}
