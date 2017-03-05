package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Student;
import java.util.Date;
import java.util.List;

/**
 * Created by Chamvaru on 18.06.2016.
 */
public interface StudentManagerIntf{

    public void addToStudent(String streetnr, String cityzip, Integer phone, Date date);
    
    public List<Student> getAllStudents();

    public Student getStudent(int studentid);

    public boolean insertStudent(Student inStudent);

    public boolean deleteStudent(Student delStudent);

    public void updateStudent(Student updateStudent);
}
