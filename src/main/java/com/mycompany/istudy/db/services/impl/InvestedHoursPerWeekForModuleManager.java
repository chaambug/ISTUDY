package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.InvestedHoursPerWeekForModuleManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Investedhoursperweekformodule;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import java.util.Collections;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Chamvaru on 17.06.2016.
 */
public class InvestedHoursPerWeekForModuleManager implements InvestedHoursPerWeekForModuleManagerIntf{

    private final EntityManager em;
    private static InvestedHoursPerWeekForModuleManager instance;
    private final static Logger logger = Logger.getLogger(InvestedHoursPerWeekForModuleManager.class);

    private InvestedHoursPerWeekForModuleManager() {
        em = Connection.getInstance().getEntityManager();

    }

    public static InvestedHoursPerWeekForModuleManager getInstance() {
        if (instance == null) {
            instance = new InvestedHoursPerWeekForModuleManager();
        }
        return instance;
    }

    @Override
    public List<Investedhoursperweekformodule> getAllEntriesForModule(Modul module, Semester activeSemester) {
        try {
            logger.info("service call getAllEntriesForModule");
            return em.createNamedQuery("Investedhoursperweekformodule.findAllByModuleAndSemester", Investedhoursperweekformodule.class)
                    .setParameter("module", module)
                    .setParameter("semester", activeSemester)
                    .getResultList();
        } catch (Exception e) {
            logger.error("getAllEntriesForModule not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Investedhoursperweekformodule getByModuleAndWeek(Modul module, int week, Semester activeSemester) {
        try {
            logger.info("service call getByModuleAndWeek");
            List<Investedhoursperweekformodule> resultList = em.createNamedQuery("Investedhoursperweekformodule.findByModuleAndWeekAndSemester", Investedhoursperweekformodule.class)
                    .setParameter("module", module)
                    .setParameter("week", week)
                    .setParameter("semester", activeSemester)
                    .getResultList();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            logger.error("getByModuleAndWeek not successfull", e);
        }
        return null;
    }

    @Override
    public void updateEntity(Investedhoursperweekformodule entity) {
        try {
            logger.info("service call updateEntity");
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("updateEntity not successfull", e);
        }
    }

    @Override
    public void insertEntity(Investedhoursperweekformodule entity) {
        try {
            logger.info("service call insertEntity");
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("insertEntity not successfull", e);
        }
    }
}