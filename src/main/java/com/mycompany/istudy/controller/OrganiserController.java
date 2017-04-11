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
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * The Controller class of the Organiser view. 
 * 
 * @author Chaam
 */
public class OrganiserController extends BaseController{

    private final static Logger LOGGER = Logger.getLogger(OrganiserController.class);
    
    GraphicalViewController graphicalViewController;
    
    public OrganiserController(UserWin instance) {
        super(instance);
        graphicalViewController = new GraphicalViewController(instance);
    }
    
    /**
     * initializes the organiser view.
     */
    @Override
    public void init() {
        instance.getWeekJComboBox().removeAllItems();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<Modul> allActiveModules = ModulManager.getInstance().getAllActiveModules(StudentManager.getInstance().getStudent());
        if (!allActiveModules.isEmpty()) {
            DefaultTableModel modelActiveModule = (DefaultTableModel) instance.getActiveModuleJTable().getModel();
            GuiServices.deleteTableContent(instance.getActiveModuleJTable());
            allActiveModules.stream().forEach((modul) -> {
                Semester semester = modul.getSemesternummer();
                String expected = "";
                try {
                    Date s = sdf.parse(semester.getSemesterStart());
                    Date e = sdf.parse(semester.getExaminationStart());
                    final long kw = GuiServices.getCalendarWeeks(s, e);
                    expected = String.valueOf(modul.getStudyhours() / kw);
                    //String verarbeitung
                    int indexOfPoint = expected.indexOf(".");
                    if (indexOfPoint != -1) {
                        expected = expected.substring(0, indexOfPoint + 2);
                    }
                } catch (Exception e) {
                    LOGGER.error("System error", e);
                }
                modelActiveModule.addRow(new Object[]{modul.getSemesternummer().getNumber(), modul.getModulname(), expected, modul.getEctspunkte(), modul.getStudyhours()});
            });
        }

        Semester activeSemester = SemesterManager.getInstance().getActiveSemester(StudentManager.getInstance().getStudent());
        if (activeSemester != null) {
            try {
                Date s = sdf.parse(activeSemester.getSemesterStart());
                Date e = sdf.parse(activeSemester.getExaminationStart());
                instance.getTotalCalenderWeeks().setText(String.valueOf(GuiServices.getCalendarWeeks(s, e)));
            } catch (Exception e) {
                LOGGER.error("System error", e);
            }
        } else {
            instance.getTotalCalenderWeeks().setText("");
        }
    }
    
     /**
     * Prepares the table where the user can enter the invested hours for each module.
     */    
    public void prepareTableToBookInvestedHours() {
        final int row = instance.getActiveModuleJTable().getSelectedRow();
        Student student = StudentManager.getInstance().getStudent();
        if (row != -1) {
            try {
                String moduleName = (String) instance.getActiveModuleJTable().getValueAt(row, 1);
                Modul modul = ModulManager.getInstance().getModulByName(moduleName, student);
                Semester activeSemester = SemesterManager.getInstance().getActiveSemester(StudentManager.getInstance().getStudent());
                updateInvestedHoursForAModuleJTable(modul, activeSemester);

                int semesterNo = Integer.parseInt(String.valueOf(instance.getActiveModuleJTable().getValueAt(row, 0)));
                Semester semesterByNumber = SemesterManager.getInstance().getSemesterByNumber(semesterNo);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date s = sdf.parse(semesterByNumber.getSemesterStart());
                Date e = sdf.parse(semesterByNumber.getExaminationStart());
                long weeks = GuiServices.getCalendarWeeks(s, e);
                instance.getWeekJComboBox().removeAllItems();
                for (int i = 1; i <= weeks; i++) {
                    instance.getWeekJComboBox().addItem(String.valueOf(i));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(instance,
                        "Date of examination period and start of semester are missing...!",
                        "Semester dates missing",
                        JOptionPane.ERROR_MESSAGE);
                LOGGER.error("System error", ex);
            }
        }
    }
    
    private void updateInvestedHoursForAModuleJTable(Modul modul, Semester activeSemester) {
        GuiServices.deleteTableContent(instance.getInvestedHoursForAModuleJTable());
        if (modul == null) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) instance.getInvestedHoursForAModuleJTable().getModel();
        InvestedHoursPerWeekForModuleManager.getInstance().getAllEntriesForModule(modul, activeSemester).stream().forEach((entry) -> {
            model.addRow(new Object[]{entry.getWeek(), entry.getInvestedHours()});
        });
    }
    /**
     * Sets the invested hours for a module which the user has inserted in a textfield of the view. 
    */   
    public void pushHours() {
        final int row = instance.getActiveModuleJTable().getSelectedRow();
        Semester activeSemester = SemesterManager.getInstance().getActiveSemester(StudentManager.getInstance().getStudent());

        if (activeSemester == null) {
            JOptionPane.showMessageDialog(instance,
                    "Please set a semester as an active one!",
                    "No active semester",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (row != -1) {
            String moduleName = (String) instance.getActiveModuleJTable().getValueAt(row, 1);
            Student student = StudentManager.getInstance().getStudent();
            Modul modul = ModulManager.getInstance().getModulByName(moduleName, student);
            try {

                if (instance.getWeekJComboBox().getItemCount() == 0) {
                    JOptionPane.showMessageDialog(instance,
                            "Date of examination period and start of semester are missing...!",
                            "Semester dates missing",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                final int investedHours = Integer.parseInt(instance.getInvestedHoursJTextField().getText());
                final int weekNr = instance.getWeekJComboBox().getSelectedIndex() + 1;
                InvestedHoursPerWeekForModuleManager service = InvestedHoursPerWeekForModuleManager.getInstance();
                Investedhoursperweekformodule investedhoursperweekforModule;
                investedhoursperweekforModule = service.getByModuleAndWeek(modul, weekNr, activeSemester);
                if (investedhoursperweekforModule != null) {
                    //update
                    investedhoursperweekforModule.setInvestedHours(investedHours);
                    service.updateEntity(investedhoursperweekforModule);
                } else {
                    //insert
                    investedhoursperweekforModule = new Investedhoursperweekformodule();
                    investedhoursperweekforModule.setInvestedHours(investedHours);
                    investedhoursperweekforModule.setModuleId(modul);
                    investedhoursperweekforModule.setWeek(weekNr);
                    investedhoursperweekforModule.setSemesterid(activeSemester);
                    service.insertEntity(investedhoursperweekforModule);
                }
                updateInvestedHoursForAModuleJTable(modul, activeSemester);
                instance.getInvestedHoursJTextField().setText("");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(instance,
                        "Check your input - Hours have to be nummeric.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                updateInvestedHoursForAModuleJTable(null, null);
                LOGGER.error("System error", nfe);
                return;
            }
            JOptionPane.showMessageDialog(instance,
                    "Invested Hours actualized for " + moduleName + "!",
                    "Pushed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(instance,
                    "Please select the module you want to push the invested hours for!",
                    "Bad access",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
