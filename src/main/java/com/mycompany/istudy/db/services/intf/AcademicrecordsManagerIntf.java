/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Academicrecords;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Student;
import java.util.List;

/**
 * nterface for the Manager class of Academicrecords entity.
 * @author Cham
 */
public interface AcademicrecordsManagerIntf {

    public void insertTry(Academicrecords obj);

    public List<Academicrecords> getAllRecords(Student student, Modul modul);

    public void removeRecord(Academicrecords obj);
    
    public Academicrecords getAcademicrecord(Student student, Modul modul);
    
    public List<Academicrecords> getAcademicrecord(Student student);
    
    public List<Academicrecords> getAcademicrecordGrad5(Student student, Modul modul);
    
    public List<Academicrecords> getAcademicrecordNot0AndNot5(Student student, Modul modul);
}
