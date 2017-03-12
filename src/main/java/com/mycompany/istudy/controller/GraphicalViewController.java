/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.db.entities.Investedhoursperweekformodule;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.services.impl.InvestedHoursPerWeekForModuleManager;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.principalservices.GraphicalView;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import java.awt.BorderLayout;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class GraphicalViewController extends BaseController {

    private final static Logger LOGGER = Logger.getLogger(GraphicalViewController.class);

    public GraphicalViewController(UserWin instance) {
        super(instance);
    }

    @Override
    public void init() {
        actualizeListOfActiveModules();
    }

    public void createGraph() {
        Object selectedItem = instance.getModuleListjComboBox().getSelectedItem();
        Student student = StudentManager.getInstance().getStudent();
        if (selectedItem != null) {
            String selectedModul = selectedItem.toString();
            Modul modul = ModulManager.getInstance().getModulByName(selectedModul, student);
            HashMap<Double, Double> hoursWeek = new HashMap<>();
            HashMap<Double, Double> optimalPerformance = new HashMap<>();
            hoursWeek.put(0.0, 0.0);
            optimalPerformance.put(0.0, 0.0);
            Semester semester = SemesterManager.getInstance().getActiveSemester(StudentManager.getInstance().getStudent());
            List<Investedhoursperweekformodule> investedList = InvestedHoursPerWeekForModuleManager.getInstance().getAllEntriesForModule(modul, semester);

            double totalHours = 0;
            for (Investedhoursperweekformodule i : investedList) {
                totalHours += i.getInvestedHours();
                hoursWeek.put((double) i.getWeek(), (double) (totalHours));
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date s = sdf.parse(semester.getSemesterStart());
                Date e = sdf.parse(semester.getExaminationStart());
                final long kw = GuiServices.getCalendarWeeks(s, e);
                final double expected = modul.getStudyhours() / kw;
                totalHours = 0;
                for (int i = 1; i <= kw; i++) {
                    totalHours += expected;
                    optimalPerformance.put((double) i, (double) totalHours);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(instance,
                        "Date of examination period and start of semester are missing...!",
                        "Semester dates missing",
                        JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Error during creation of graphs", e);
            }
            GraphicalView graphicalView = new GraphicalView("Module to Week", selectedModul, hoursWeek, optimalPerformance);
            instance.getModulPerformancejPanel().removeAll();
            Component chartPanel = graphicalView.getRootPane();
            instance.getModulPerformancejPanel().add(chartPanel, BorderLayout.CENTER);
            instance.getModulPerformancejPanel().validate();
            instance.getModulPerformancejPanel().setVisible(true);
        }
    }

    public void actualizeListOfActiveModules() {
        StudentManager studentManager = StudentManager.getInstance();
        Student student = studentManager.getStudent();
        ModulManager modulManager = ModulManager.getInstance();
        List<Modul> modulList = modulManager.getAllActiveModules(student);
        instance.getModuleListjComboBox().removeAllItems();
        modulList.forEach((m) -> {
            instance.getModuleListjComboBox().addItem(m.getModulname());
        });
    }
}
