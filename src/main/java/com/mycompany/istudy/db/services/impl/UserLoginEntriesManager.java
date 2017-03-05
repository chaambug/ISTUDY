/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.UserLoginEntriesManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.entities.Userloginentries;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class UserLoginEntriesManager implements UserLoginEntriesManagerIntf{

    private final EntityManager em;
    private static UserLoginEntriesManager instance;

    private final static Logger logger = Logger.getLogger(UserLoginEntriesManager.class);

    public UserLoginEntriesManager(EntityManager em) {
        this.em = em;
    }

    public static UserLoginEntriesManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return (instance = new UserLoginEntriesManager(Connection.getInstance().getEntityManager()));
    }

    @Override
    public void insertLoginEntry(Userloginentries obj) {
        try {
            logger.info("service call insertLoginEntry");
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("insertLoginEntry not successfull", e);
        }
    }

    @Override
    public List<Userloginentries> getLoginEntriesForUser(Student student) {
        try {
            logger.info("service call getLoginEntriesForUser");
            return em.createNamedQuery("Userloginentries.findByStudent", Userloginentries.class).setParameter("student", student).getResultList();
        } catch (Exception e) {
            logger.error("getLoginEntriesForUser not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }
}
