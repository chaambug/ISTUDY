/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.db.entities.Academicrecords;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.entities.Userloginentries;
import com.mycompany.istudy.db.services.impl.AcademicrecordsManager;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.db.services.impl.UserLoginEntriesManager;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class HomeController extends BaseController {

    private final static Logger LOGGER = Logger.getLogger(HomeController.class);

    public HomeController(UserWin instance) {
        super(instance);
    }

    @Override
    public void init() {
        StudentManager sm = StudentManager.getInstance();
        Student s = sm.getStudent();
        instance.getHomeusername().setText(s.getBenutzername());
        instance.getHomefirstname().setText(s.getVorname());
        instance.getHomelastname().setText(s.getNachname());
        instance.getHomeuniversity().setText(s.getNameOfUni());
        instance.getHomefaculty().setText(s.getNameOfFac());
        instance.getHomesubject().setText(s.getSubjectStream());
        instance.getHomematno().setText(String.valueOf(s.getMatrikelnummer()));
        UserLoginEntriesManager userLoginInstance = UserLoginEntriesManager.getInstance();
        List<Userloginentries> loginEntriesForUser = userLoginInstance.getLoginEntriesForUser(s);
        //Show in table
        DefaultTableModel model = (DefaultTableModel) instance.getLoginentriesTable().getModel();
        GuiServices.deleteTableContent(instance.getLoginentriesTable());
        loginEntriesForUser.stream().forEach((login) -> {
            model.addRow(new Object[]{login.getLogindate().substring(10), login.getLogindate().substring(0, 10)});
        });
        //insert login to db
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Userloginentries userloginentry = new Userloginentries();
        userloginentry.setLogindate(sdf.format(new Date()));
        userloginentry.setUserid(s);
        userLoginInstance.insertLoginEntry(userloginentry);

        initInfoBoard();
    }

    public int totalDaysToExam() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date todaysDate = new Date();
        int days = 0;
        try {
            Date e = sdf.parse(SemesterManager.getInstance().getActiveSemester(StudentManager.getInstance().getStudent()).getExaminationStart());
            days = (int) GuiServices.getCalenderDays(todaysDate, e);
        } catch (ParseException ex) {
            LOGGER.error("System error", ex);
        }
        return days;
    }

    public void initInfoBoard() {
        try {
            //helper
            Student student;
            List<Modul> allActiveModules;
            student = StudentManager.getInstance().getStudent();
            allActiveModules = ModulManager.getInstance().getAllActiveModules(student);
            //set active semester
            Semester activeSemester = SemesterManager.getInstance().getActiveSemester(student);
            instance.getActiveSemesterJLabelValue().setText(activeSemester == null ? "" : "" + activeSemester.getNumber());

            //next coming exam date
            List<Academicrecords> academicrecordList = new ArrayList<>();
            allActiveModules.stream()
                    .map((modul) -> AcademicrecordsManager.getInstance()
                    .getAcademicrecord(student, modul))
                    .filter((academicrecord)
                            -> (academicrecord != null))
                    .forEachOrdered((Academicrecords academicrecord) -> {
                        academicrecordList.add(academicrecord);
                    });

            Collections.sort(academicrecordList, (Academicrecords o1, Academicrecords o2) -> {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    String examinationdateOfO1 = o1.getExaminationdate();
                    String examinationdateOfO2 = o2.getExaminationdate();

                    Date date1 = sdf.parse(examinationdateOfO1);
                    Date date2 = sdf.parse(examinationdateOfO2);
                    return date1.compareTo(date2);
                } catch (ParseException ex) {
                    LOGGER.error("System error", ex);
                }
                return -1;
            });

            if (!academicrecordList.isEmpty()) {
                Academicrecords ar = academicrecordList.get(0);
                instance.getNextExamJLabelValue().setText(ar.getExaminationdate() + " - " + ar.getModuleid().getModulname());
            }
            //Total of active modules
            for (int i = 0; i < allActiveModules.size(); i++) {
                int totalModules = 0;
                totalModules += i;
                instance.getActiveModulesJLabelValue().setText(Integer.toString(totalModules));
            }

            //Noten Durchnitt
            for (Modul m : allActiveModules) {
                //List<Academicrecords> academicrecordsList = m.getAcademicrecordsList();
                for (Academicrecords academicrecords : academicrecordList) {
                    double result = 0;
                    double totalOfECTS = 0;
                    for (int i = 0; i < academicrecordList.size(); i++) {
                        result += (academicrecords.getGrade() * academicrecords.getModuleid().getEctspunkte());
                        totalOfECTS = result / allActiveModules.size();
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("System error", ex);
        }
    }
}
