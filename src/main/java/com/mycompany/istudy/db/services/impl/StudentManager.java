package com.mycompany.istudy.db.services.impl;

import com.mycompany.istudy.db.services.intf.StudentManagerIntf;
import com.mycompany.istudy.db.connection.Connection;
import com.mycompany.istudy.db.entities.Student;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Manager class for the Entity  Student
 * Created by Cham on 18.06.2016.
 */
public class StudentManager implements StudentManagerIntf{

    private final EntityManager em;
    private Student student;
    private static StudentManager instance;
    private final static Logger LOGGER = Logger.getLogger(StudentManager.class);

    private StudentManager(EntityManager em) {
        this.em = em;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    /**
     * returns instance of this class (Singleton)
     * @return instance of StudentManager
     */
    public static StudentManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return (instance = new StudentManager(Connection.getInstance().getEntityManager()));
    }

    @Override
    public void addToStudent(String streetnr, String cityzip, Integer phone, Date date) {
        try {
            LOGGER.info("service call addToStudent");
            student.setStreetNr(streetnr);
            student.setCityAndZip(cityzip);
            student.setGeburtsdatum(date);
            updateStudent(student);
        } catch (Exception e) {
            LOGGER.error("addToStudent not successfull", e);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        try {
            LOGGER.info("service call getAllStudents");
            return em != null ? em.createNamedQuery("Student.findAll", Student.class).getResultList() : null;
        } catch (Exception e) {
            LOGGER.error("getAllStudents students not successfull", e);
        }
        return null;
    }

    @Override
    public Student getStudent(int studentid) {
        try {
            LOGGER.info("service call getStudent - id : " + studentid);
            return em.find(Student.class, studentid);
        } catch (Exception e) {
            LOGGER.error("getStudent not successfull", e);
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
            LOGGER.info("service call insertStudent - user : " + inStudent.getBenutzername());
            em.getTransaction().begin();
            em.persist(inStudent);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            LOGGER.error("insertStudent not successfull", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteStudent(Student delStudent) {
        try {
            LOGGER.info("service call deleteStudent - user : " + delStudent.getBenutzername());
            em.getTransaction().begin();
            em.remove(delStudent);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("deleteStudent not successfull", e);
            return false;
        }
        return true;
    }

    @Override
    public void updateStudent(Student updateStudent) {
        try {
            LOGGER.info("service call updateStudent - user : " + updateStudent.getBenutzername());
            em.getTransaction().begin();
            em.merge(updateStudent);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("updateStudent not successfull", e);
        }
    }

    @Override
    public Student isValidUser(String user, String pwd) {
         try {
            LOGGER.info("service call isValidUser - user : " + user + " - Password length : " + pwd.length());
            List<Student> students = em.createNamedQuery("Student.isValid", Student.class)
                    .setParameter("username", user)
                    .setParameter("password", pwd)
                    .getResultList();
            if (students != null && students.size() > 0) {
                return students.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("isValidUser not successfull", e);
        }
        return null;
    }
}
