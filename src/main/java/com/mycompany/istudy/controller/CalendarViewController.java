package com.mycompany.istudy.controller;

/**
 * Created by Varuni on 22.02.2017.
 */
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.services.impl.StudentManager;
import javax.swing.*;   
import javax.swing.table.DefaultTableModel;
import java.util.GregorianCalendar;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.TableCalendarRenderer;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewController extends BaseController {

    private static int realYear, currentYear, currentMonth;
    private ArrayList<Double> investedHoursPerDayOfActualMonth;

    public CalendarViewController(UserWin instance) {
        super(instance);
    }

    @Override
    public void init() {
        //TableCalendarRenderer tCalendarrenderer = new TableCalendarRenderer();
        DefaultTableModel modelCalendarTable = (DefaultTableModel) instance.getcalendarjTable().getModel();
        JScrollPane scrollTableCalendar = instance.getCalendarjScrollPane(modelCalendarTable);
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i = 0; i < 7; i++) {
            modelCalendarTable.addColumn(headers[i]);
        }
        scrollTableCalendar.setVisible(true);

        //No resize/reorder
        instance.getcalendarjTable().getTableHeader().setResizingAllowed(false);
        instance.getcalendarjTable().getTableHeader().setReorderingAllowed(false);

        //Single cell selection
        instance.getcalendarjTable().setColumnSelectionAllowed(true);
        instance.getcalendarjTable().setRowSelectionAllowed(true);
        instance.getcalendarjTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Set row/column count
        instance.getcalendarjTable().setRowHeight(60);
        modelCalendarTable.setColumnCount(7);
        modelCalendarTable.setRowCount(6);

        //Populate table
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        int theRealMonth = cal.get(GregorianCalendar.MONTH); //Get month
        int theRealYear = cal.get(GregorianCalendar.YEAR); //Get year
        instance.getCalendarjComboBox().removeAllItems();
        for (int i = theRealYear - 100; i <= theRealYear + 100; i++) {
            instance.getCalendarjComboBox().addItem(String.valueOf(i));
        }
        refreshCalendar(theRealMonth, theRealYear);
        actualizeListOfActiveModulesForCalendar();
    }

    public void refreshCalendar(int month, int year) {
        Student student = StudentManager.getInstance().getStudent();
        Object selectedItem = instance.getModuleListjComboBox().getSelectedItem();
        if (selectedItem != null) {
            String selectedModul = selectedItem.toString();
            if (selectedModul != null) {
                Modul modul = ModulManager.getInstance().getModulByName(selectedModul, student);
                investedHoursPerDayOfActualMonth = new ArrayList<>();
                //TODO: get list of hours for modul and month from DB
                for (int i = 0; i < 31; i++) {
                    investedHoursPerDayOfActualMonth.add(1.5);
                }
            } else {
                investedHoursPerDayOfActualMonth = null;
            }
            //TODO: get invested Hours of month from modul (DB) for actual month

            //Variables
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            int nod, som; //Number Of Days, Start Of Month

            //Allow/disallow buttons
            instance.getPreviousjButton().setEnabled(true);
            instance.getNextjButton().setEnabled(true);
            if (month == 0 && year <= realYear - 10) {
                instance.getPreviousjButton().setEnabled(false);
            } //Too early
            if (month == 11 && year >= realYear + 100) {
                instance.getNextjButton().setEnabled(false);
            } //Too late
            instance.getMonthjLabel().setText(months[month]); //Refresh the month label (at the top)
            instance.getMonthjLabel().setBounds(160 - instance.getMonthjLabel().getPreferredSize().width / 2, 25, 180, 25); //Re-align label with calendar
            instance.getCalendarjComboBox().setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box

            //Clear table
            DefaultTableModel modelCalendarTable = (DefaultTableModel) instance.getcalendarjTable().getModel();
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    modelCalendarTable.setValueAt(null, i, j);
                }
            }
            //Get first day of month and number of days
            GregorianCalendar cal = new GregorianCalendar(year, month, 1);
            nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            som = cal.get(GregorianCalendar.DAY_OF_WEEK);

            //Draw calendar
            for (int i = 1; i <= nod; i++) {
                int row = (i + som - 2) / 7;
                int column = (i + som - 2) % 7;
                if (investedHoursPerDayOfActualMonth == null) {
                    modelCalendarTable.setValueAt(i, row, column);
                } else {
                    modelCalendarTable.setValueAt(i + "  sdfolizseflkgjhsdfgjkhsdfkgjshdfglkjsdhfglkjsdfhglsdkjf  " + investedHoursPerDayOfActualMonth.get(i - 1) + " Std.", row, column);
                }

            }

            //Apply renderers
            instance.getcalendarjTable().setDefaultRenderer(instance.getcalendarjTable().getColumnClass(0), new TableCalendarRenderer());
        }
    }

    public void nextMonth() {
        if (currentMonth == 11) { //Foward one year
            currentMonth = 0;
            currentYear += 1;
        } else { //Foward one month
            currentMonth += 1;
        }
        refreshCalendar(currentMonth, currentYear);
    }

    public void previousMonth() {
        if (currentMonth == 0) { //Back one year
            currentMonth = 11;
            currentYear -= 1;
        } else { //Back one month
            currentMonth -= 1;
        }
        refreshCalendar(currentMonth, currentYear);
    }

    public void changeYear() {
        if (instance.getCalendarjComboBox().getSelectedItem() != null) {
            String b = instance.getCalendarjComboBox().getSelectedItem().toString();
            currentYear = Integer.parseInt(b);
            refreshCalendar(currentMonth, currentYear);
        }
    }

    public void actualizeListOfActiveModulesForCalendar() {
        StudentManager studentManager = StudentManager.getInstance();
        Student student = studentManager.getStudent();
        ModulManager modulManager = ModulManager.getInstance();
        List<Modul> modulList = modulManager.getAllActiveModules(student);
        instance.getModuleListCalendarjComboBox().removeAllItems();
        modulList.stream().forEach((module) -> {
            instance.getModuleListCalendarjComboBox().addItem(module.getModulname());
        });
    }

    public void showInvestedHoursForModule() {

        refreshCalendar(currentMonth, currentYear);

    }
}
