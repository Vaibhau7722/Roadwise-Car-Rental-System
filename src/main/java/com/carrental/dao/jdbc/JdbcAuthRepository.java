package com.carrental.dao.jdbc;

import com.carrental.model.UserSession;
import com.carrental.util.Database;
import java.sql.*;

public class JdbcAuthRepository {
    public UserSession authenticate(String email) throws SQLException {
        String sql="SELECT u.id,u.email,u.password_hash,u.role,c.id AS customer_id,c.full_name FROM users u LEFT JOIN customers c ON c.user_id=u.id WHERE u.email=? AND u.active=TRUE";
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,email);try(ResultSet r=p.executeQuery()){if(!r.next())return null;return new UserSession(r.getLong("id"),r.getLong("customer_id"),r.getString("email"),r.getString("full_name"),r.getString("role"));}}
    }
    public String passwordHash(String email) throws SQLException {
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("SELECT password_hash FROM users WHERE email=? AND active=TRUE")){p.setString(1,email);try(ResultSet r=p.executeQuery()){return r.next()?r.getString(1):null;}}
    }
    public void register(String fullName,String email,String phone,String passwordHash) throws SQLException {
        String userSql="INSERT INTO users(email,password_hash,role) VALUES(?,?,'CUSTOMER')";
        String customerSql="INSERT INTO customers(user_id,full_name,phone) VALUES(?,?,?)";
        try(Connection c=Database.getConnection()){c.setAutoCommit(false);try(PreparedStatement u=c.prepareStatement(userSql,Statement.RETURN_GENERATED_KEYS);PreparedStatement customer=c.prepareStatement(customerSql)){
            u.setString(1,email);u.setString(2,passwordHash);u.executeUpdate();try(ResultSet keys=u.getGeneratedKeys()){if(!keys.next())throw new SQLException("Could not create user");customer.setLong(1,keys.getLong(1));customer.setString(2,fullName);customer.setString(3,phone);customer.executeUpdate();}c.commit();
        }catch(SQLException ex){c.rollback();throw ex;}finally{c.setAutoCommit(true);}}
    }
    public void updateProfile(long customerId,String name,String phone) throws SQLException {try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("UPDATE customers SET full_name=?,phone=? WHERE id=?")){p.setString(1,name);p.setString(2,phone);p.setLong(3,customerId);if(p.executeUpdate()!=1)throw new SQLException("Customer profile not found");}}
    public void changePassword(long userId,String hash) throws SQLException {try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("UPDATE users SET password_hash=? WHERE id=? AND active=TRUE")){p.setString(1,hash);p.setLong(2,userId);if(p.executeUpdate()!=1)throw new SQLException("User account not found");}}
    public boolean verifyPassword(long userId,String raw) throws SQLException {try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("SELECT password_hash FROM users WHERE id=?")){p.setLong(1,userId);try(ResultSet r=p.executeQuery()){return r.next()&&com.carrental.util.PasswordUtil.matches(raw,r.getString(1));}}}
    public java.util.Map<String,Object> profile(long customerId) throws SQLException {String sql="SELECT c.full_name,c.phone,u.email FROM customers c JOIN users u ON c.user_id=u.id WHERE c.id=?";try(Connection cn=Database.getConnection();PreparedStatement p=cn.prepareStatement(sql)){p.setLong(1,customerId);try(ResultSet r=p.executeQuery()){if(!r.next())return null;var m=new java.util.LinkedHashMap<String,Object>();m.put("fullName",r.getString(1));m.put("phone",r.getString(2));m.put("email",r.getString(3));return m;}}}
}
