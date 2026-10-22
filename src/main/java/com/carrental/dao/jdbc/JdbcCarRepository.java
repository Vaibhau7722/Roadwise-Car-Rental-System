package com.carrental.dao.jdbc;

import com.carrental.util.Database;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class JdbcCarRepository {
    private static final String FIELDS="SELECT id,brand,model,registration_number,category,fuel_type,transmission,seating_capacity,daily_rate,status,image_url FROM cars";
    public List<Map<String,Object>> search(String text,String category,BigDecimal maxRate,boolean availableOnly) throws SQLException {
        StringBuilder sql=new StringBuilder(FIELDS).append(" WHERE 1=1");List<Object> args=new ArrayList<>();
        if(availableOnly)sql.append(" AND status='AVAILABLE'");
        if(text!=null&&!text.isBlank()){sql.append(" AND (brand LIKE ? OR model LIKE ?)");args.add("%"+text.trim()+"%");args.add("%"+text.trim()+"%");}
        if(category!=null&&!category.isBlank()){sql.append(" AND category=?");args.add(category);}
        if(maxRate!=null){sql.append(" AND daily_rate<=?");args.add(maxRate);}
        sql.append(" ORDER BY status='AVAILABLE' DESC,brand,model");
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(sql.toString())){for(int i=0;i<args.size();i++)p.setObject(i+1,args.get(i));try(ResultSet r=p.executeQuery()){List<Map<String,Object>> rows=new ArrayList<>();while(r.next())rows.add(car(r));return rows;}}
    }
    public Map<String,Object> find(long id) throws SQLException {try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(FIELDS+" WHERE id=?")){p.setLong(1,id);try(ResultSet r=p.executeQuery()){return r.next()?car(r):null;}}}
    public List<Map<String,Object>> categories() throws SQLException {try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement("SELECT DISTINCT category FROM cars ORDER BY category");ResultSet r=p.executeQuery()){List<Map<String,Object>> out=new ArrayList<>();while(r.next())out.add(Map.of("name",r.getString(1)));return out;}}
    private Map<String,Object> car(ResultSet r)throws SQLException {Map<String,Object> m=new LinkedHashMap<>();m.put("id",r.getLong("id"));m.put("brand",r.getString("brand"));m.put("model",r.getString("model"));m.put("registrationNumber",r.getString("registration_number"));m.put("category",r.getString("category"));m.put("fuelType",r.getString("fuel_type"));m.put("transmission",r.getString("transmission"));m.put("seatingCapacity",r.getInt("seating_capacity"));m.put("dailyRate",r.getBigDecimal("daily_rate"));m.put("status",r.getString("status"));m.put("imageUrl",r.getString("image_url"));return m;}
}
