package com.carrental.dao.jdbc;

import com.carrental.util.Database;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcDashboardRepository {
    public Map<String,Object> adminSummary()throws SQLException {String sql="SELECT (SELECT COUNT(*) FROM cars) total_cars,(SELECT COUNT(*) FROM cars WHERE status='AVAILABLE') available_cars,(SELECT COUNT(*) FROM customers) total_customers,(SELECT COUNT(*) FROM bookings) total_bookings,(SELECT COUNT(*) FROM bookings WHERE status='ACTIVE') active_rentals,(SELECT COALESCE(SUM(amount),0) FROM payments WHERE payment_status='SIMULATED_PAID') total_revenue";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){r.next();Map<String,Object>m=new LinkedHashMap<>();m.put("totalCars",r.getLong("total_cars"));m.put("availableCars",r.getLong("available_cars"));m.put("totalCustomers",r.getLong("total_customers"));m.put("totalBookings",r.getLong("total_bookings"));m.put("activeRentals",r.getLong("active_rentals"));m.put("totalRevenue",r.getBigDecimal("total_revenue"));return m;}}
    public Map<String,Object> customerSummary(long customerId)throws SQLException {String sql="SELECT COUNT(*) total_bookings,SUM(status='CONFIRMED') upcoming,SUM(status='ACTIVE') active,SUM(status='COMPLETED') completed FROM bookings WHERE customer_id=?";try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,customerId);try(ResultSet r=p.executeQuery()){r.next();Map<String,Object>m=new LinkedHashMap<>();m.put("totalBookings",r.getLong("total_bookings"));m.put("upcoming",r.getLong("upcoming"));m.put("active",r.getLong("active"));m.put("completed",r.getLong("completed"));return m;}}}
}
