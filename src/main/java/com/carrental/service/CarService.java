package com.carrental.service;

import com.carrental.dao.hibernate.HibernateCarRepository;
import com.carrental.dao.jdbc.JdbcCarRepository;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.exception.CarNotFoundException;
import com.carrental.exception.ValidationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.URI;

public class CarService {
    private static final Pattern REG=Pattern.compile("^[A-Za-z0-9 -]{3,25}$");
    private final JdbcCarRepository reader; private final HibernateCarRepository writer;
    public CarService(JdbcCarRepository reader,HibernateCarRepository writer){this.reader=reader;this.writer=writer;}
    public List<Map<String,Object>> search(String q,String category,BigDecimal max,boolean available)throws SQLException{return reader.search(q,category,max,available);}
    public List<String> categories(){return writer.categories();}
    public Map<String,Object> find(long id)throws SQLException {if(id<1)throw new CarNotFoundException("Car not found.");Map<String,Object> car=reader.find(id);if(car==null)throw new CarNotFoundException("Car not found.");return car;}
    public Car save(String id,String brand,String model,String reg,String category,String fuel,String transmission,String seats,String rate,String status,String image)throws SQLException {
        String b=clean(brand,60,"Brand"),m=clean(model,80,"Model"),r=clean(reg,25,"Registration number").toUpperCase(Locale.ROOT),cat=clean(category,40,"Category"),f=clean(fuel,30,"Fuel type"),t=clean(transmission,30,"Transmission");
        if(!REG.matcher(r).matches())throw new ValidationException("Registration number must be 3 to 25 letters, numbers, spaces, or hyphens.");int capacity;BigDecimal price;try{capacity=Integer.parseInt(seats);price=new BigDecimal(rate);}catch(Exception ex){throw new ValidationException("Enter a valid seating capacity and daily price.");}if(capacity<1||capacity>20)throw new ValidationException("Seating capacity must be between 1 and 20.");if(price.signum()<=0||price.scale()>2)throw new ValidationException("Daily price must be positive and use at most two decimal places.");CarStatus carStatus;try{carStatus=CarStatus.valueOf(status);}catch(Exception ex){throw new ValidationException("Choose a valid car status.");}
        Car car;if(id==null||id.isBlank())car=new Car();else{try{car=writer.findById(Long.parseLong(id)).orElseThrow(()->new CarNotFoundException("Car not found."));}catch(NumberFormatException e){throw new CarNotFoundException("Car not found.");}}
        String safeImage=validateImage(image);
        car.setBrand(b);car.setModel(m);car.setRegistrationNumber(r);car.setCategory(cat);car.setFuelType(f);car.setTransmission(t);car.setSeatingCapacity(capacity);car.setDailyRate(price);car.setStatus(carStatus);car.setImageUrl(safeImage);
        try{if(car.getId()==null)return writer.save(car);return writer.update(car);}catch(org.hibernate.exception.ConstraintViolationException ex){throw new ValidationException("That registration number is already assigned to another car.");}
    }
    public void deactivate(long id){Car car=writer.findById(id).orElseThrow(()->new CarNotFoundException("Car not found."));car.setStatus(CarStatus.INACTIVE);writer.update(car);}
    private String clean(String v,int max,String field){if(v==null||v.trim().isEmpty()||v.trim().length()>max)throw new ValidationException(field+" is required and must be at most "+max+" characters.");return v.trim();}
    private String validateImage(String value){if(value==null||value.isBlank())return null;String image=value.trim();if(image.startsWith("/assets/images/")&&!image.contains(".."))return image;try{URI uri=URI.create(image);if("https".equalsIgnoreCase(uri.getScheme())&&uri.getHost()!=null)return uri.toASCIIString();}catch(IllegalArgumentException ignored){}throw new ValidationException("Image must be an HTTPS URL or a path under /assets/images/.");}
}
