package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.ModulManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

/**
 * Manager class for the Entity Modul
 * Created by Cham on 17.06.2016.
 */
public class ModulManager implements ModulManagerIntf{

    private final EntityManager em;
    private static ModulManager instance;
    private final static Logger LOGGER = Logger.getLogger(ModulManager.class);

    private ModulManager() {
        em = Connection.getInstance().getEntityManager();

    }
    
    /**
     * returns instance of this class (Singleton)
     * @return instance of ModulManager
     */
    public static ModulManager getInstance() {
        if (instance == null) {
            instance = new ModulManager();
        }
        return instance;
    }
    
    /**
     * Gets all modules.
     * @return all modules as list.
     */
    @Override
    public List<Modul> getAllModule() {
        try {
            LOGGER.info("service call getAllModule");
            return em != null ? em.createNamedQuery("Modul.findAll", Modul.class).getResultList() : null;
        } catch (Exception e) {
            LOGGER.error("getAllModule not successfull", e);
        }
        return null;
    }

    /**
     * Gets all modules of the given student
     * @return all modules as list.
     */    
    @Override
    public List<Modul> getAllModule(Student student) {
        try {
            LOGGER.info("service call getAllModule");
            return em != null ? em.createNamedQuery("Modul.findByStudent", Modul.class).setParameter("student", student).getResultList() : new ArrayList<>();
        } catch (Exception e) {
            LOGGER.error("getAllModule not successfull", e);
        }
        return new ArrayList<>();
    }
    
     /**
     * Gets all modules of a specific Student by matrikelnummer.
     * @param matrikelnummer the number for the student.
     * @return list of all modules the student has.
     */
    @Override
    public List<Modul> getModuleByMatrikelnummer(int matrikelnummer) {
        try {
            LOGGER.info("service call getModuleByStudiengangId - id : " + matrikelnummer);
            Query query = em.createQuery("SELECT e FROM Student e WHERE e.matrikelnummer=:arg1");
            query.setParameter("arg1", matrikelnummer);
            List<Modul> modulList = getAllModule();
            for (Modul m : modulList) {
                if (m.getMatrikelnummer().equals(matrikelnummer)) {
                    return modulList;
                }
                
            }
        } catch (Exception e) {
            LOGGER.error("getModuleByMatrikelnummer Module not successfull", e);
        }
        return null;
    }

    @Override
    public Modul getModul(int modulid) {
        try {
            LOGGER.info("service call getModul - id : " + modulid);
            return em.find(Modul.class, modulid);
        } catch (Exception e) {
            LOGGER.error("getModul not successfull", e);
        }
        return null;
    }

    @Override
    public void insertModul(Modul inModul) {
        try {
            LOGGER.info("service call insertModul - name : " + inModul.getModulname());
            em.getTransaction().begin();
            em.persist(inModul);
            em.getTransaction().commit();
            em.detach(inModul);
        } catch (Exception e) {
            LOGGER.error("insertModul not successfull", e);
        }
    }

    @Override
    public void deleteModul(Modul inModul) {
        try {
            LOGGER.info("service call deleteModul - name : " + inModul.getModulname());
            em.getTransaction().begin();
            em.remove(inModul);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("deleteModul not successfull", e);
        }
    }

    @Override
    public void updateModul(Modul inModul) {
        try {
            LOGGER.info("service call updateModul - name" + inModul.getModulname());
            em.getTransaction().begin();
            em.merge(inModul);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("updateModul not successfull", e);
        }
    }

    @Override
    public List<Modul> getAllActiveModules(Student student) {
        try {
            LOGGER.info("service call getAllActiveModules");
            return em.createNamedQuery("Modul.findByStatusAndStudent", Modul.class)
                    .setParameter("value", true)
                    .setParameter("student", student)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllActiveModules not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<Modul> getAllModule(Semester semester, Student s) {
        try {
            LOGGER.info("service call getAllModule");
            return em.createNamedQuery("Modul.findAllModulesByStudentAndSemester", Modul.class)
                    .setParameter("semester", semester)
                    .setParameter("student", s)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllModule not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deleteModules(Student student, Semester semester) {
        try {
            LOGGER.info("service call deleteModules");
            List<Modul> allModules = getAllModule();
            em.getTransaction().begin();
            allModules
                    .stream()
                    .filter((m) -> (Objects.equals(m.getSemesternummer().getId(), semester.getId()) && Objects.equals(m.getMatrikelnummer().getMatrikelnummer(), student.getMatrikelnummer())))
                    .forEach((m) -> {
                        em.remove(m);
                    });
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("deleteModules not successfull", e);
        }
    }

    @Override
    public Modul getModulByName(String modulName, Student student) {
        try {
            LOGGER.info("service call getModulByName");
            List<Modul> modulList = getAllModule();
            for (Modul m : modulList) {
                if (m.getModulname().equals(modulName) && Objects.equals(m.getMatrikelnummer().getMatrikelnummer(), student.getMatrikelnummer())) {
                    return m;
                }
            }
        } catch (Exception e) {
            LOGGER.error("getModulByName not successfull", e);
        }
        return null;
    }
}
