/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.utils;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Matteo
 */
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    final static Logger LOGGER = LogManager.getLogger(HibernateUtil.class);

    public static SessionFactory buildSessionFactory() {
        try {
            Configuration conf = new Configuration();
            conf.configure("com/mpedy/utils/hibernate.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
            return conf.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSessionFromSchema(String schema) {
        Session result = null;
        try {
            result = sessionFactory.openSession();
            result.createSQLQuery("SET search_path TO " + schema).executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error HibernateUtil.getSessionFactory(): " + e.toString());
        }
        if (result == null) {
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
            } catch (Exception e) {
                LOGGER.error("Error HibernateUtil.getSessionFactory Redirect: " + e.toString());
            }
        }
        return result;
    }
}
