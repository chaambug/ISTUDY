package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import java.util.List;

/**
 * Created by Chamvaru on 17.06.2016.
 */
public interface ModulManagerIntf {

    public List<Modul> getAllModule();
    
    public List<Modul> getAllModule(Student student);

    public List<Modul> getModuleByMatrikelnummer(int matrikelnummer);

    public Modul getModul(int modulid);

    public void insertModul(Modul inModul);

    public void deleteModul(Modul inModul);

    public void updateModul(Modul inModul);

    public List<Modul> getAllActiveModules(Student student);

    public Iterable<Modul> getAllModule(Semester semester, Student s);

    public void deleteModules(Student student, Semester semester);

    public Modul getModulByName(String modulName, Student student);
}
