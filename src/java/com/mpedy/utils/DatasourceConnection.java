/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpedy.utils;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DatasourceConnection {

    private static DataSource datasource = null;
    private static final Logger logger = LogManager.getLogger(DatasourceConnection.class.getName());
   
    public DatasourceConnection() {
    }

    public static Connection getConnection() {

        try {
            if (datasource == null) {
                DatasourceConnection.initDatasource();
            }
            Connection conn = null;
            conn = datasource.getConnection();
            return conn;
        } catch (SQLException e) {
            logger.error("Errore in connessione! " + e.getMessage());
            return null;
        }
    }

    public static void initDatasource() {

        //spostare su properties esterne
        
        PoolProperties p = new PoolProperties();

        p.setUrl("jdbc:postgresql://localhost:5432/stix");
        p.setDriverClassName("org.postgresql.Driver");
        p.setUsername("stix");
        p.setPassword("stix");

        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("select * from pg_stat_activity");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);

        p.setMaxActive(10);
        p.setMaxIdle(10);
        p.setInitialSize(4);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(4);

        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);

        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);
        logger.info("Db connection estabilished");

    }

    public static void closeDatasource() {
        datasource.close();
        logger.info("Db connection closed");
    }
}
