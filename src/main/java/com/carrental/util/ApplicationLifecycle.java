package com.carrental.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ApplicationLifecycle implements ServletContextListener {
    private static final Logger log=LoggerFactory.getLogger(ApplicationLifecycle.class);
    @Override public void contextInitialized(ServletContextEvent event){
        try { HibernateUtil.getSessionFactory(); try(Connection databaseConnection=Database.getConnection()){log.info("Car Rental database connection is ready.");} }
        catch(RuntimeException|java.sql.SQLException ex){log.error("Car Rental could not initialize its database services. Set DB_URL, DB_USERNAME, DB_PASSWORD and load database/schema.sql before using the app.",ex);}
    }
    @Override public void contextDestroyed(ServletContextEvent event){HibernateUtil.shutdown();Database.close();log.info("Car Rental application resources closed.");}
}
