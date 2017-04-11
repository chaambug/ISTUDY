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
 * Manager class for the Entity InvestedHoursPerWeekForModule
 * Created by Cham on 17.06.2016.
 */
public class InvestedHoursPerWeekForModuleManager implements InvestedHoursPerWeekForModuleManagerIntf{

    private final EntityManager em;
    private static InvestedHoursPerWeekForModuleManager instance;
    private final static Logger LOGGER = Logger.getLogger(InvestedHoursPerWeekForModuleManager.class);

    private InvestedHoursPerWeekForModuleManager() {
        em = Connection.getInstance().getEntityManager();

    }
    
     /**
     * return instance of this class (Singleton)
     * @return instance of InvestedHoursPerWeekForModuleManager
     */
    public static InvestedHoursPerWeekForModuleManager getInstance() {
        if (instance == null) {
            instance = new InvestedHoursPerWeekForModuleManager();
        }
        return instance;
    }
    
    /**
     * Gets all invested hours peer week entries for a module in an active semester.
     * @param module the module for which the entries will be returned.
     * @param activeSemester the active semester.
     * @return list of Investedhoursperweekformodule
     */
    @Override
    public List<Investedhoursperweekformodule> getAllEntriesForModule(Modul module, Semester activeSemester) {
        try {
            LOGGER.info("service call getAllEntriesForModule");
            return em.createNamedQuery("Investedhoursperweekformodule.findAllByModuleAndSemester", Investedhoursperweekformodule.class)
                    .setParameter("module", module)
                    .setParameter("semester", activeSemester)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllEntriesForModule not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Gets invested hours for a week for a module in a semester. 
     * @param module the module for the invested hours.
     * @param week the week in which the invested hours were invested.
     * @param activeSemester the semster in which these hours where invested.
     * @return the invested hours in the week for the module. 
     */
    @Override
    public Investedhoursperweekformodule getByModuleAndWeek(Modul module, int week, Semester activeSemester) {
        try {
            LOGGER.info("service call getByModuleAndWeek");
            List<Investedhoursperweekformodule> resultList = em.createNamedQuery("Investedhoursperweekformodule.findByModuleAndWeekAndSemester", Investedhoursperweekformodule.class)
                    .setParameter("module", module)
                    .setParameter("week", week)
                    .setParameter("semester", activeSemester)
                    .getResultList();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            LOGGER.error("getByModuleAndWeek not successfull", e);
        }
        return null;
    }
    
    /**
     * Updates the entity Investedhoursperweekformodule
     * 
     * @param entity the entity to be updated.
     */
    @Override
    public void updateEntity(Investedhoursperweekformodule entity) {
        try {
            LOGGER.info("service call updateEntity");
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("updateEntity not successfull", e);
        }
    }
    
    /**
     * Inserts the entity Investedhoursperweekformodule.
     * @param entity entity to be inserted.
     */
    @Override
    public void insertEntity(Investedhoursperweekformodule entity) {
        try {
            LOGGER.info("service call insertEntity");
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("insertEntity not successfull", e);
        }
    }
}
