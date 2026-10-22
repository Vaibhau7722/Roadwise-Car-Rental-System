package com.carrental.dao.hibernate;

import com.carrental.dao.CrudRepository;
import com.carrental.entity.Car;
import com.carrental.entity.CarStatus;
import com.carrental.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateCarRepository implements CrudRepository<Car,Long> {
    @Override public Car save(Car car){return inTransaction(s->{s.persist(car);return car;});}
    @Override public Car update(Car car){return inTransaction(s->{s.merge(car);return car;});}
    @Override public Optional<Car> findById(Long id){try(Session s=HibernateUtil.getSessionFactory().openSession()){return Optional.ofNullable(s.get(Car.class,id));}}
    public List<String> categories(){try(Session s=HibernateUtil.getSessionFactory().openSession()){return s.createSelectionQuery("select distinct c.category from Car c where c.status <> :inactive order by c.category",String.class).setParameter("inactive",CarStatus.INACTIVE).getResultList();}}
    private <T>T inTransaction(java.util.function.Function<Session,T> action){Transaction tx=null;try(Session s=HibernateUtil.getSessionFactory().openSession()){tx=s.beginTransaction();T value=action.apply(s);tx.commit();return value;}catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;}}
}
