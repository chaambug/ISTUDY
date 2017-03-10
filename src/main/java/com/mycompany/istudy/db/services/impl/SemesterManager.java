/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.SemesterManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Varuni
 */
public class SemesterManager implements SemesterManagerIntf{

    private final EntityManager em;
    private static SemesterManager instance;
    private final static Logger LOGGER = Logger.getLogger(SemesterManager.class);

    public SemesterManager(EntityManager em) {
        this.em = em;
    }

    public static SemesterManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return (instance = new SemesterManager(Connection.getInstance().getEntityManager()));
    }

    @Override
    public List<Semester> getAllSemesterOfStudent(Student student) {
        try {
            LOGGER.info("service call getAllSemesterOfStudent");
            return em.createNamedQuery("Semester.findSemesterByStudent", Semester.class)
                    .setParameter("student", student)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllSemesterOfStudent not successfull", e);
        }
        return null;

    }

    @Override
    public void insertSemester(Semester obj) {
        try {
            LOGGER.info("service call insertSemester");
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("insertSemester not successfull", e);
        }
    }

    @Override
    public void deleteSemester(int nr) {
        try {
            LOGGER.info("service call deleteSemester");
            List<Semester> resultList = em.createNativeQuery("select * from semester where number = " + nr, Semester.class).getResultList();
            Student student = StudentManager.getInstance().getStudent();
            for (Semester s : resultList) {
                if (Objects.equals(s.getUserid().getMatrikelnummer(), student.getMatrikelnummer())) {
                    em.getTransaction().begin();
                    em.remove(s);
                    em.getTransaction().commit();
                    return;
                }
            }
        } catch (Exception e) {
            LOGGER.error("deleteSemester not successfull", e);
        }
    }

    @Override
    public Semester getSemesterByNumber(int number) {
        try {
            LOGGER.info("service call getSemesterByNumber");
            List<Semester> resultList = em.createNativeQuery("select * from semester where number = " + number, Semester.class).getResultList();
            Student student = StudentManager.getInstance().getStudent();
            for (Semester s : resultList) {
                if (Objects.equals(s.getUserid().getMatrikelnummer(), student.getMatrikelnummer())) {
                    return s;
                }
            }
        } catch (Exception e) {
            LOGGER.error("getSemesterByNumber not successfull", e);
        }
        return null;
    }

    @Override
    public void updateSemester(Semester semester) {
        try {
            LOGGER.info("service call updateSemester");
            em.getTransaction().begin();
            em.merge(semester);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("updateSemester not successfull", e);
        }
    }

    @Override
    public List<Semester> getAllSemester() {
        try {
            LOGGER.info("service call getAllSemester");
            return em.createNamedQuery("Semester.findAll", Semester.class).getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllSemester not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void setSemesterActive(int semester, boolean b) {
        try {
            LOGGER.info("service call setSemesterActive");
            List<Semester> allSemester = getAllSemester();
            em.getTransaction().begin();
            allSemester.stream().forEach((s) -> {
                if (s.getNumber() == semester) {
                    s.setSemesterstatus(b);
                } else {
                    s.setSemesterstatus(false);
                }
            });
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("setSemesterActive not successfull", e);
        }
    }

    @Override
    public Semester getActiveSemester(Student student) {
        try {
            LOGGER.info("service call getActiveSemester");
            List<Semester> resultList = em.createNamedQuery("Semester.findActiveSemesterAndStudent", Semester.class)
                    .setParameter("student", student)
                    .getResultList();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            LOGGER.error("getActiveSemester not successfull", e);
        }
        return null;
    }
}
