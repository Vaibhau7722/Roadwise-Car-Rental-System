package com.carrental.service;
import com.carrental.dao.jdbc.JdbcReportRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
public class ReportService {private final JdbcReportRepository repo;public ReportService(JdbcReportRepository repo){this.repo=repo;}public List<Map<String,Object>> monthlyRevenue()throws SQLException{return repo.monthlyRevenue();}public List<Map<String,Object>> mostRented()throws SQLException{return repo.mostRented();}}
