/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.db.entities.Academicrecords;
import com.mycompany.istudy.db.entities.Investedhoursperweekformodule;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.entities.Userloginentries;
import com.mycompany.istudy.db.services.impl.AcademicrecordsManager;
import com.mycompany.istudy.db.services.impl.InvestedHoursPerWeekForModuleManager;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        UserLoginEntriesManager userLoginInstance = UserLoginEntriesManager
                .getInstance();
        List<Userloginentries> loginEntriesForUser = userLoginInstance
                .getLoginEntriesForUser(s);
        //Show in table
        DefaultTableModel model = (DefaultTableModel) instance.getLoginentriesTable().getModel();
        GuiServices.deleteTableContent(instance.getLoginentriesTable());
        loginEntriesForUser.stream().forEach((login) -> {
            model.addRow(new Object[]{login.getLogindate().substring(10),
                login.getLogindate().substring(0, 10)});
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
            Date e = sdf.parse(SemesterManager.getInstance()
                    .getActiveSemester(StudentManager.getInstance()
                            .getStudent()).getExaminationStart());
            days = (int) GuiServices.getCalenderDays(todaysDate, e);
        } catch (ParseException ex) {
            LOGGER.error("System error", ex);
        }
        return days;
    }

    public void initInfo() {
        initInfoBoard();
        initDetails();
        initReport();
    }

    public void initInfoBoard() {
        try {
            //helper
            Student student;
            List<Modul> allActiveModules;

            student = StudentManager.getInstance().getStudent();
            allActiveModules = ModulManager.getInstance()
                    .getAllActiveModules(student);

            //set active semester
            Semester activeSemester = SemesterManager.getInstance()
                    .getActiveSemester(student);
            instance.getActiveSemesterJLabelValue()
                    .setText(activeSemester == null ? "0" : ""
                            + activeSemester.getNumber());

            //next coming exam date
            final List<Academicrecords> academicrecordList = new ArrayList<>();
            allActiveModules
                    .stream()
                    .map((modul) -> AcademicrecordsManager.getInstance().getAcademicrecord(student, modul))
                    .filter((academicrecord) -> (academicrecord != null && !academicrecord.getExaminationdate().isEmpty()))
                    .forEachOrdered((Academicrecords academicrecord) -> {
                        academicrecordList.add(academicrecord);
                    });

            Collections.sort(academicrecordList,
                    (Academicrecords o1, Academicrecords o2) -> {
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
                instance.getNextExamJLabelValue()
                        .setText(ar.getExaminationdate() + " - "
                                + ar.getModuleid().getModulname());
            } else {
                instance.getNextExamJLabelValue()
                        .setText("");
            }

            //Total of active modules
            instance.getActiveModulesJLabelValue().setText(String.valueOf(allActiveModules.size()));

            //Average of total points            
            double result;
            double sumOfMultiplicationOfGradeAndCredits = 0.0;
            double sumOfAllSavedCredits = 0.0;
            double sumOfAllCreditsToAchieve = 0.0;
            int sumeOfPassedExams = 0;
            int sumeOfFailedExams = 0;
            final List<Academicrecords> academicrecordListForStudent = AcademicrecordsManager.getInstance().getAcademicrecord(student);
            //Wenn Grade der ar nicht 0,0 ist oder nicht 5,0 ist, auch 
            //arList nicht leer ist wird die berechnung ausgeführt!     
            for (Academicrecords ar : academicrecordListForStudent) {
                if (!(ar.getGrade() == 0.0 || ar.getGrade() == 5.0)) {
                    sumOfMultiplicationOfGradeAndCredits += ar.getGrade() * ar.getModuleid().getEctspunkte();
                    sumOfAllSavedCredits += ar.getModuleid().getEctspunkte();
                    sumeOfPassedExams++;
                } else {
                    sumOfAllCreditsToAchieve += ar.getModuleid().getEctspunkte();
                    if (ar.getGrade() == 5.0) {
                        sumeOfFailedExams++;
                    }
                }
            }

            result = sumOfMultiplicationOfGradeAndCredits / sumOfAllSavedCredits;
            instance.getAverageJLabelValue()
                    .setText(Double.toString(sumOfAllSavedCredits == 0 ? 0 : result));

            //Credit points to be achieved
            instance.getSavedcpJLabelValue().setText(String.valueOf(sumOfAllSavedCredits));

            //achieved credit points
            instance.getCpToAchieveJLabelValue().setText(String.valueOf(sumOfAllCreditsToAchieve));

            //No.of failed exams
            instance.getFailedExamsJLabelValue().setText(String.valueOf(sumeOfFailedExams));

            //No.of passed exams
            instance.getPassedExamsJLabelValue().setText(String.valueOf(sumeOfPassedExams));

        } catch (Exception ex) {
            LOGGER.error("System error", ex);
        }
    }

    private void initDetails() {

        //module and grade table
        Student student = StudentManager.getInstance().getStudent();
        List<Academicrecords> list = AcademicrecordsManager.getInstance().getAcademicrecord(student);
        list = list
                .stream()
                .filter(record -> (record.getGrade() != 0.0 && record.getModuleid().getModulestatus()))
                .collect(Collectors.toList());

        Collections.sort(list, (Academicrecords o1, Academicrecords o2) -> Double.compare(o1.getGrade(), o2.getGrade()));

        final DefaultTableModel moduleGradeJTableModel = (DefaultTableModel) instance.getModuleGradeJTable().getModel();
        GuiServices.deleteTableContent(instance.getModuleGradeJTable());
        list.stream().forEach((recored) -> {
            moduleGradeJTableModel.addRow(new Object[]{recored.getModuleid().getModulname(),
                recored.getGrade()});
        });

        //module and try table
        List<Modul> modules = ModulManager.getInstance().getAllModule(student);
        Map<String, String> modulTryList = new HashMap<>();

        modules.forEach((modul) -> {
            modulTryList.put(
                    modul.getModulname(),
                    String.valueOf(AcademicrecordsManager.getInstance().getAcademicrecordGrad5(student, modul).size() + 1)
                    + ";"
                    + (AcademicrecordsManager.getInstance().getAcademicrecordNot0AndNot5(student, modul).isEmpty() ? "Open" : "Passed"));
        });

        final DefaultTableModel moduleTryJTableModel = (DefaultTableModel) instance.getModuleTryJTable().getModel();
        GuiServices.deleteTableContent(instance.getModuleTryJTable());
        modulTryList.keySet().forEach((key) -> {
            String[] values = modulTryList.get(key).split(";");
            moduleTryJTableModel.addRow(new Object[]{
                key, values[0], values[1]});
        });
    }

    private void initReport() {
        instance.getReportArea().setText("Reportings...\n");
        final String reportMsg = "Missing invested hours for module %s (Missing weeks : %s)\n";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            final Student student = StudentManager.getInstance().getStudent();
            final Semester semester = SemesterManager.getInstance().getActiveSemester(student);
            if (semester != null) {
                Date d1 = sdf.parse(semester.getSemesterStart());
                Date d2 = new Date();
                if (d2.after(d1)) {
                    long weeks = GuiServices.getCalendarWeeks(d1, d2);
                    Iterator<Modul> modul = ModulManager.getInstance().getAllModule(semester, student).iterator();
                    while (modul.hasNext()) {
                        Modul m = modul.next();
                        if (m.getModulestatus()) {
                            List<Investedhoursperweekformodule> invested = InvestedHoursPerWeekForModuleManager.getInstance().getAllEntriesForModule(m, semester);
                            long investedWeeks = invested.size();
                            if (weeks > investedWeeks) {
                                instance.getReportArea().append(String.format(reportMsg, m.getModulname(), weeks - investedWeeks));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during reporting", e);
        }
    }
}
