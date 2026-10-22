package com.carrental.service;

import com.carrental.dao.jdbc.JdbcBookingRepository;
import com.carrental.exception.BookingNotFoundException;
import com.carrental.exception.ValidationException;
import com.carrental.model.BookingReceipt;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final JdbcBookingRepository repo;private final RentalPricingService pricing;
    public BookingService(JdbcBookingRepository repo,RentalPricingService pricing){this.repo=repo;this.pricing=pricing;}
    public BookingReceipt create(long customerId,String car,String pickup,String returned)throws SQLException {long carId;LocalDate start,end;try{carId=Long.parseLong(car);start=LocalDate.parse(pickup);end=LocalDate.parse(returned);}catch(Exception e){throw new ValidationException("Choose a car and valid pickup and return dates.");}if(carId<1)throw new ValidationException("Choose a valid car.");pricing.validateBookingDates(start,end,LocalDate.now());int days=pricing.rentalDays(start,end);return repo.create(customerId,carId,start,end,rate->pricing.rentalCost(rate,days));}
    public List<Map<String,Object>> forCustomer(long id)throws SQLException{return repo.forCustomer(id);}
    public List<Map<String,Object>> all(String term,String status)throws SQLException {if(status!=null&&!status.isBlank()&&!List.of("CONFIRMED","ACTIVE","COMPLETED","CANCELLED").contains(status))throw new ValidationException("Choose a valid booking status.");return repo.all(term,status);}
    public Map<String,Object> customerBooking(long customerId,String id)throws SQLException {long bookingId=parseId(id);Map<String,Object> b=repo.customerBooking(customerId,bookingId);if(b==null)throw new BookingNotFoundException("Booking not found.");return b;}
    public Map<String,Object> invoice(long customerId,String id)throws SQLException {Map<String,Object>b=customerBooking(customerId,id);if(!List.of("COMPLETED","ACTIVE").contains(b.get("status")))throw new ValidationException("An invoice is available after pickup has been recorded.");return b;}
    public boolean cancelCustomer(long customerId,String id)throws SQLException{return repo.cancelCustomer(customerId,parseId(id));}
    public boolean cancelAdmin(String id)throws SQLException{return repo.cancelAdmin(parseId(id));}
    public Map<String,Object> booking(String id)throws SQLException {Map<String,Object>b=repo.booking(parseId(id));if(b==null)throw new BookingNotFoundException("Booking not found.");return b;}
    public void pickup(String id)throws SQLException{repo.pickup(parseId(id));}
    public void returnRental(String id)throws SQLException{repo.returnRental(parseId(id));}
    private long parseId(String id){try{long v=Long.parseLong(id);if(v>0)return v;}catch(Exception ignored){}throw new BookingNotFoundException("Booking not found.");}
}
