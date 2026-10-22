package com.carrental.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HibernateUtil {
    private static final Logger log=LoggerFactory.getLogger(HibernateUtil.class);
    private static volatile SessionFactory sessionFactory;
    private HibernateUtil() {}
    public static SessionFactory getSessionFactory() {
        if(sessionFactory==null || sessionFactory.isClosed()) synchronized(HibernateUtil.class) {
            if(sessionFactory==null || sessionFactory.isClosed()) {
                String url=System.getenv("DB_URL"), user=System.getenv("DB_USERNAME"), password=System.getenv("DB_PASSWORD");
                if(url==null || url.isBlank() || user==null || user.isBlank()) {
                    String msg="Hibernate cannot initialize: set DB_URL, DB_USERNAME, and DB_PASSWORD."; log.error(msg); throw new IllegalStateException(msg);
                }
                try { Configuration c=new Configuration().configure("hibernate.cfg.xml");
                    c.setProperty("hibernate.connection.url",url).setProperty("hibernate.connection.username",user)
                      .setProperty("hibernate.connection.password",password==null?"":password);
                    sessionFactory=c.buildSessionFactory();
                } catch(RuntimeException ex) { log.error("Hibernate initialization failed. Check database reachability and schema.sql.",ex); throw ex; }
            }
        }
        return sessionFactory;
    }
    public static synchronized void shutdown(){if(sessionFactory!=null&&!sessionFactory.isClosed())sessionFactory.close();sessionFactory=null;}
}
