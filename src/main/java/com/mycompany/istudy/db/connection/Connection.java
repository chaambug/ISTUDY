package com.mycompany.istudy.db.connection;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * The Connection class creates and holds the connection to the database.
 * Created by Cham on 17.06.2016.
 */
public class Connection {

    private final static Logger LOGGER = Logger.getLogger(Connection.class);
    private static Connection connection;
    private EntityManager em;

    private Connection() {
        try {
            em = Persistence.createEntityManagerFactory("istudydb").createEntityManager();
            LOGGER.info("singleton connection setup to database");
        } catch (Exception e) {
            LOGGER.error("Error during creation connection pool to the database", e);
        }
    }
    
    /**
     * 
     * Initializes a new database connection.
     */
    public static void initInstance() {
        if (connection == null) {
            connection = new Connection();
        }
    }
    
    /**
     * Returns the connection instance.
     * @return instance of database connection.
     */
    public static Connection getInstance() {
        initInstance();
        return connection;
    }
    
    /**
     * Returns the EntityManaer
     * @return EntityManaer
     */
    public EntityManager getEntityManager() {
        return em;
    }
}
