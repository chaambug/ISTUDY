/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.AcademicrecordsManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Academicrecords;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Manager class for the Entity Academicrecords
 * @author Cham
 */
public class AcademicrecordsManager implements AcademicrecordsManagerIntf {

    private final EntityManager em;
    private static AcademicrecordsManager instance;
    private final static Logger LOGGER = Logger.getLogger(AcademicrecordsManager.class);

    private AcademicrecordsManager() {
        em = Connection.getInstance().getEntityManager();
    }

    public static AcademicrecordsManager getInstance() {
        if (instance == null) {
            instance = new AcademicrecordsManager();
        }
        return instance;
    }
    
     /**
     * return instance of this class (Singleton)
     * @return instance of AcademicrecordsManager
     */
    @Override
    public void insertTry(Academicrecords obj) {
        try {
            LOGGER.info("service call insertTry");
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("insertTry not successfull", e);
        }
    }
    
    /**
     * Gets all Academicrecords for a given module and student.
     * @param student the student for which the records are.
     * @param modul the modul for which the records are.
     * @return a list of the Academicrecords.
     */
    @Override
    public List<Academicrecords> getAllRecords(Student student, Modul modul) {
        try {
            LOGGER.info("service call getAllRecords");
            return em.createNamedQuery("Academicrecords.findAllRecords", Academicrecords.class)
                    .setParameter("student", student)
                    .setParameter("module", modul)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.error("getAllRecords not successfull", e);
        }
        return Collections.EMPTY_LIST;
    }
    
     /**
     * Removes a record.
     * @param obj record to be removed.
     */
    @Override
    public void removeRecord(Academicrecords obj) {
        try {
            LOGGER.info("service call removeRecord");
            em.getTransaction().begin();
            em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("removeRecord not successfull", e);
        }
    }
    /**
     * Get all academic records for the given modul and student
     * @param student
     * @param modul
     * @return list of academic records
     */
    @Override 
    public Academicrecords getAcademicrecord(Student student, Modul modul) {
        try {
            LOGGER.info("service call getAcademicrecord");
            List<Academicrecords> resultList = em.createNamedQuery("Academicrecords.findRecordForStudentAndModul", Academicrecords.class)
                    .setParameter("student", student)
                    .setParameter("module", modul)
                    .getResultList();
            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("getAcademicrecord not successfull", e);
        }
        return null;
    }
    
    /**
     * Get all academic records which include the grade 5 (failed exam) 
     * for the given modul and student
     * @param student
     * @param modul
     * @return list of academic records
     */
    @Override
    public List<Academicrecords> getAcademicrecordGrad5(Student student, Modul modul) {
        try {
            LOGGER.info("service call getAcademicrecordGrad5");
            List<Academicrecords> resultList = em.createNamedQuery("Academicrecords.findAllRecordsForStudentAndModulWithGrad5", Academicrecords.class)
                    .setParameter("student", student)
                    .setParameter("module", modul)
                    .getResultList();
                return resultList;
        } catch (Exception e) {
            LOGGER.error("getAcademicrecordGrad5 not successfull", e);
        }
        return new ArrayList<>();
    }
    
     /**
     * Get all academic records which exclude the grade 5 and 0 (passed exam) 
     * for the given modul and student
     * @param student
     * @param modul
     * @return list of academic records
     */    
    @Override
    public List<Academicrecords> getAcademicrecordNot0AndNot5(Student student, Modul modul) {
        try {
            LOGGER.info("service call getAcademicrecordNot0AndNot5");
            List<Academicrecords> resultList = em.createNamedQuery("Academicrecords.findAllRecordsForStudentAndModulNot0AndNot5", Academicrecords.class)
                    .setParameter("student", student)
                    .setParameter("module", modul)
                    .getResultList();
                return resultList;
        } catch (Exception e) {
            LOGGER.error("getAcademicrecordNot0AndNot5 not successfull", e);
        }
        return new ArrayList<>();
    }
    
     /**
     * Get all the academic records for the given student
     * @param student
     * @param modul
     * @return list of academic records
     */
    @Override
    public List<Academicrecords> getAcademicrecord(Student student) {
        try {
            LOGGER.info("service call getAcademicrecord for the student");
            List<Academicrecords> resultList = em.createNamedQuery("Academicrecords.findByStudent", Academicrecords.class)
                    .setParameter("student", student)
                    .getResultList();

            return resultList;

        } catch (Exception e) {
            LOGGER.error("getAcademicrecord not successfull", e);
        }
        return new ArrayList<>();
    }

}
