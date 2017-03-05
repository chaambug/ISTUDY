package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.StudentManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Student;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by Chamvaru on 18.06.2016.
 */
public class StudentManager implements StudentManagerIntf{

    private final EntityManager em;
    private Student student;
    private static StudentManager instance;
    private final static Logger logger = Logger.getLogger(StudentManager.class);

    private StudentManager(EntityManager em) {
        this.em = em;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public static StudentManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return (instance = new StudentManager(Connection.getInstance().getEntityManager()));
    }

    @Override
    public void addToStudent(String streetnr, String cityzip, Integer phone, Date date) {
        try {
            logger.info("service call addToStudent");
            student.setStreetNr(streetnr);
            student.setCityAndZip(cityzip);
            student.setGeburtsdatum(date);
            updateStudent(student);
        } catch (Exception e) {
            logger.error("addToStudent not successfull", e);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        try {
            logger.info("service call getAllStudents");
            return em != null ? em.createNamedQuery("Student.findAll", Student.class).getResultList() : null;
        } catch (Exception e) {
            logger.error("getAllStudents students not successfull", e);
        }
        return null;
    }

    @Override
    public Student getStudent(int studentid) {
        try {
            logger.info("service call getStudent - id : " + studentid);
            return em.find(Student.class, studentid);
        } catch (Exception e) {
            logger.error("getStudent not successfull", e);
        }
        return null;
    }

    /**
     *
     * @param inStudent
     * @return
     */
    @Override
    public boolean insertStudent(Student inStudent) {
        try {
            logger.info("service call insertStudent - user : " + inStudent.getBenutzername());
            em.getTransaction().begin();
            em.persist(inStudent);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            logger.error("insertStudent not successfull", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteStudent(Student delStudent) {
        try {
            logger.info("service call deleteStudent - user : " + delStudent.getBenutzername());
            em.getTransaction().begin();
            em.remove(delStudent);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("deleteStudent not successfull", e);
            return false;
        }
        return true;
    }

    @Override
    public void updateStudent(Student updateStudent) {
        try {
            logger.info("service call updateStudent - user : " + updateStudent.getBenutzername());
            em.getTransaction().begin();
            em.merge(updateStudent);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("updateStudent not successfull", e);
        }
    }
}
