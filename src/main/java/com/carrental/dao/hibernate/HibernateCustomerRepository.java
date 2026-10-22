package com.carrental.dao.hibernate;

import com.carrental.dao.CrudRepository;
import com.carrental.entity.Customer;
import com.carrental.util.HibernateUtil;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateCustomerRepository implements CrudRepository<Customer,Long> {
    @Override public Customer save(Customer customer){return write(s->{s.persist(customer);return customer;});}
    @Override public Customer update(Customer customer){return write(s->s.merge(customer));}
    @Override public Optional<Customer> findById(Long id){try(Session s=HibernateUtil.getSessionFactory().openSession()){return Optional.ofNullable(s.get(Customer.class,id));}}
    private <T>T write(java.util.function.Function<Session,T> f){Transaction tx=null;try(Session s=HibernateUtil.getSessionFactory().openSession()){tx=s.beginTransaction();T result=f.apply(s);tx.commit();return result;}catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;}}
}
