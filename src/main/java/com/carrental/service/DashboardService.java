package com.carrental.service;
import com.carrental.dao.jdbc.JdbcDashboardRepository;
import java.sql.SQLException;
import java.util.Map;
public class DashboardService {private final JdbcDashboardRepository repo;public DashboardService(JdbcDashboardRepository repo){this.repo=repo;}public Map<String,Object> admin()throws SQLException{return repo.adminSummary();}public Map<String,Object> customer(long id)throws SQLException{return repo.customerSummary(id);}}
