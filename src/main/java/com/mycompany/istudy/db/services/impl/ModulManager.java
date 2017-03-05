package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.ModulManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import java.util.Collections;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chamvaru on 17.06.2016.
 */
public class ModulManager implements ModulManagerIntf{

    private final EntityManager em;
    private static ModulManager instance;
    private final static Logger logger = Logger.getLogger(ModulManager.class);

    private ModulManager() {
        em = Connection.getInstance().getEntityManager();

    }

    public static ModulManager getInstance() {
        if (instance == null) {
            instance = new ModulManager();
        }
        return instance;
    }

    @Override
    public List<Modul> getAllModule() {
        try {
            logger.info("service call getAllModule");
            return em != null ? em.createNamedQuery("Modul.findAll", Modul.class).getResultList() : null;
        } catch (Exception e) {
            logger.error("getAllModule not successfull", e);
        }
        return null;
    }

    @Override
    public List<Modul> getModuleByMatrikelnummer(int matrikelnummer) {
        try {
            logger.info("service call getModuleByStudiengangId - id : " + matrikelnummer);
            Query query = em.createQuery("SELECT e FROM Student e WHERE e.matrikelnummer=:arg1");
            query.setParameter("arg1", matrikelnummer);
            List<Modul> modulList = getAllModule();
            for (Modul m : modulList) {
                if (m.getMatrikelnummer().equals(matrikelnummer)) {
                }
                return modulList;
            }
        } catch (Exception e) {
            logger.error("getModuleByMatrikelnummer Module not successfull", e);
        }
        return null;
    }

    @Override
    public Modul getModul(int modulid) {
        try {
            logger.info("service call getModul - id : " + modulid);
            return em.find(Modul.class, modulid);
        } catch (Exception e) {
            logger.error("getModul not successfull", e);
        }
        return null;
    }

    @Override
    public void insertModul(Modul inModul) {
        try {
            logger.info("service call insertModul - name : " + inModul.getModulname());
            em.getTransaction().begin();
            em.persist(inModul);
            em.getTransaction().commit();
            em.detach(inModul);
        } catch (Exception e) {
            logger.error("insertModul not successfull", e);
        }
    }

    @Override
    public void deleteModul(Modul inModul) {
        try {
            logger.info("service call deleteModul - name : " + inModul.getModulname());
            em.getTransaction().begin();
            em.remove(inModul);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("deleteModul not successfull", e);
        }
    }

    @Override
    public void updateModul(Modul inModul) {
        try {
            logger.info("service call updateModul - name" + inModul.getModulname());
            em.getTransaction().begin();
            em.merge(inModul);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("updateModul not successfull", e);
        }
    }

    @Override
    public List<Modul> getAllActiveModules(Student student) {
        try {
            logger.info("service call getAllActiveModules");
            return em.createNamedQuery("Modul.findByStatusAndStudent", Modul.class)
                    .setParameter("value", true)
                    .setParameter("student", student)
                    .getResultList();
        } catch (Exception e) {
            logger.error("getAllActiveModules not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<Modul> getAllModule(Semester semester, Student s) {
        try {
            logger.info("service call getAllModule");
            return em.createNamedQuery("Modul.findAllModulesByStudentAndSemester", Modul.class)
                    .setParameter("semester", semester)
                    .setParameter("student", s)
                    .getResultList();
        } catch (Exception e) {
            logger.error("getAllModule not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void deleteModules(Student student, Semester semester) {
        try {
            logger.info("service call deleteModules");
            List<Modul> allModules = getAllModule();
            em.getTransaction().begin();
            for (Modul m : allModules) {
                if (Objects.equals(m.getSemesternummer().getId(), semester.getId()) && Objects.equals(m.getMatrikelnummer().getMatrikelnummer(), student.getMatrikelnummer())) {
                    em.remove(m);
                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("deleteModules not successfull", e);
        }
    }

    @Override
    public Modul getModulByName(String text) {
        try {
            logger.info("service call getModulByName");
            List<Modul> modulList = getAllModule();

            for (Modul m : modulList) {
                if (m.getModulname().equals(text)) {
                    return m;
                }
            }
        } catch (Exception e) {
            logger.error("getModulByName not successfull", e);
        }
        return null;
    }
}
