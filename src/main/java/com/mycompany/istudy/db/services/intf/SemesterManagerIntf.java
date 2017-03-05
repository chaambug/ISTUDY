/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import java.util.List;

/**
 *
 * @author Varuni
 */
public interface SemesterManagerIntf {
    
    public List<Semester> getAllSemesterOfStudent(Student student);
    
    public void insertSemester(Semester obj);

    public void deleteSemester(int nr);

    public Semester getSemesterByNumber(int number);

    public void updateSemester(Semester semester);

    public List<Semester> getAllSemester();

    public void setSemesterActive(int semester, boolean b);

    public Semester getActiveSemester(Student student);
}
