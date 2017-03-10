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
import com.mycompany.istudy.db.services.impl.AcademicrecordsManager;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class SubjectStreamController extends BaseController {

    private final static Logger LOGGER = Logger.getLogger(SubjectStreamController.class);

    OrganiserController organiserController;

    public SubjectStreamController(UserWin instance) {
        super(instance);
        organiserController = new OrganiserController(instance);
    }

    @Override
    public void init() {
        initPanels();
        updateSemesterAndModulTreeView();
    }

    private void initPanels() {
        instance.getModuleBoardPanel().setVisible(false);
        instance.getSemesterBoardPanel().setVisible(false);
    }

    public void updateSemesterAndModulTreeView() {
        StudentManager studentManager = StudentManager.getInstance();
        SemesterManager semesterManager = SemesterManager.getInstance();
        Student s = studentManager.getStudent();
        final List<Semester> allSemesterOfStudent = semesterManager.getAllSemesterOfStudent(s);
        DefaultTreeModel model = (DefaultTreeModel) instance.getModulAndSemesterOverviewJTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        root.setUserObject(s.getSubjectStream());
        allSemesterOfStudent.stream().map((semester) -> {
            DefaultMutableTreeNode semesterNode = new DefaultMutableTreeNode(String.format("Semester %s", semester.getNumber()));
            for (Modul modul : ModulManager.getInstance().getAllModule(semester, s)) {
                semesterNode.add(new DefaultMutableTreeNode(modul.getModulname()));
            }
            return semesterNode;
        }).forEach((semesterNode) -> {
            root.add(semesterNode);
        });
        model.nodeChanged(root);
        model.reload(root);
        expandAllNodes(instance.getModulAndSemesterOverviewJTree());
    }

    public void expandAllNodes(JTree tree) {
        int j = tree.getRowCount();
        int i = 0;
        while (i < j) {
            tree.expandRow(i);
            i += 1;
            j = tree.getRowCount();
        }
    }

    public void createModul() {
        DefaultTreeModel model = (DefaultTreeModel) instance.getModulAndSemesterOverviewJTree().getModel();
        TreePath selectionPath = instance.getModulAndSemesterOverviewJTree().getSelectionPath();
        if (selectionPath != null) {
            String nameOfNode = String.valueOf(((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject());
            String no = nameOfNode.replace("Semester ", "");
            try {
                int numberOfSelectedSemester = Integer.parseInt(no);
                String name = instance.getModulename().getText();
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(instance,
                            "Please enter the name of your module",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String ect = instance.getCreditpoints().getText();
                String hours = instance.getStudyhours().getText();
                Student student = StudentManager.getInstance().getStudent();
                Semester semester = SemesterManager.getInstance().getSemesterByNumber(numberOfSelectedSemester);
                Modul m = new Modul();
                try {
                    m.setEctspunkte(Integer.parseInt(ect));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(instance,
                            "Credit points should be in number format",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("System error", e);
                    return;
                }
                m.setMatrikelnummer(student);
                m.setModulname(name);
                m.setSemesternummer(semester);
                try {
                    m.setStudyhours(Double.valueOf(hours.replace(",", ".")));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(instance,
                            "Study hours should be in number format",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("System error", e);
                    return;
                }
                List<Modul> allModules = ModulManager.getInstance().getAllModule();
                for (Modul module : allModules) {
                    if (module.getModulname().equals(instance.getModulename().getText())) {
                        JOptionPane.showMessageDialog(instance,
                                "Module already exists! ",
                                "Multiple entry error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                ModulManager.getInstance().insertModul(m);
                updateSemesterAndModulTreeView();
                instance.getModulename().setText("");
                instance.getCreditpoints().setText("");
                instance.getStudyhours().setText("");
                return;
            } catch (NumberFormatException | HeadlessException e) {
                LOGGER.error("System error", e);
            }
        }
        JOptionPane.showMessageDialog(instance,
                "Please select a semester to create module for",
                "Selection error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setSemesterStartDate() {
        try {
            final String semesterNo = instance.getSemesterLabel().getText().trim().replace("Semester ", "");
            SemesterManager semesterManager = SemesterManager.getInstance();
            Semester semester = semesterManager.getSemesterByNumber(Integer.parseInt(semesterNo));
            Calendar calender = instance.getSemesterStartDateJCalender().getCalendar();
            if (calender != null) {
                Date date = calender.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = sdf.format(date);
                semester.setSemesterStart(formatedDate);
                semesterManager.updateSemester(semester);
            }
            initSemesterBoard();
            organiserController.init();
        } catch (Exception e) {
            LOGGER.error("System error", e);
        }
    }

    public void setExaminationStartDate() {
        try {
            final String semesterNo = instance.getSemesterLabel().getText().trim().replace("Semester ", "");
            SemesterManager semesterManager = SemesterManager.getInstance();
            Semester semester = semesterManager.getSemesterByNumber(Integer.parseInt(semesterNo));
            if (semester != null) {
                Calendar calender = instance.getExaminationStartDateJCalender().getCalendar();
                if (calender != null) {
                    Date date = calender.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String formatedDate = sdf.format(date);
                    semester.setExaminationStart(formatedDate);
                    semesterManager.updateSemester(semester);
                }
                initSemesterBoard();
                organiserController.init();
            }
        } catch (Exception e) {
            LOGGER.error("System error", e);
        }
    }

    public void activateDeactivateSemester() {
        if (ButtonConstants.ACTIVATE.toString().equals(instance.getActivateDeactivateSemesterButton().getText())) {
            activateDeactivateSemester(instance.getCurrentSemesterJLabel().getText(), true);
        } else {
            activateDeactivateSemester(instance.getCurrentSemesterJLabel().getText(), false);
        }
        initSemesterBoard();
        organiserController.init();
    }

    private void activateDeactivateSemester(String currentSemester, boolean value) {
        if (!instance.getCurrentSemesterJLabel().getText().isEmpty()) {
            instance.getCurrentSemesterJLabel().setForeground(Color.BLUE);
            int semester = Integer.parseInt(currentSemester.replace("Semester ", ""));
            SemesterManager.getInstance().setSemesterActive(semester, value);
        }
    }

    public void activateDeactivateModule() {
        if (ButtonConstants.ACTIVATE.toString().equals(instance.getActivateDeactivateModuleButton().getText())) {
            activateDeactivateModule(instance.getSelectedModuleLabel().getText(), true);
        } else {
            activateDeactivateModule(instance.getSelectedModuleLabel().getText(), false);
        }
        initModuleBoard(null);
        organiserController.init();
    }

    private void activateDeactivateModule(String moduleName, boolean value) {
        if (!instance.getSelectedModuleLabel().getText().isEmpty()) {
            instance.getSelectedModuleLabel().setForeground(Color.BLUE);
            ModulManager moduleManager = ModulManager.getInstance();
            Modul module = moduleManager.getModulByName(moduleName);
            module.setModulestatus(value);
            moduleManager.updateModul(module);
        }
    }

    public void deleteSemester() {
        DefaultTreeModel model = (DefaultTreeModel) instance.getModulAndSemesterOverviewJTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        int number = root.getChildCount();
        TreePath selectionPath = instance.getModulAndSemesterOverviewJTree().getSelectionPath();
        if (selectionPath != null) {
            String nameOfNode = String.valueOf(((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject());
            String no = nameOfNode.replace("Semester ", "");
            try {
                int numberOfSelectedSemester = Integer.parseInt(no);
                if (number == numberOfSelectedSemester) {
                    ModulManager.getInstance().deleteModules(StudentManager.getInstance().getStudent(), SemesterManager.getInstance().getSemesterByNumber(number));
                    SemesterManager.getInstance().deleteSemester(number);
                    updateSemesterAndModulTreeView();
                    organiserController.init();
                    return;
                } else {
                    JOptionPane.showMessageDialog(instance,
                            "You only can remove the last semester",
                            "Selection error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException | HeadlessException e) {
                LOGGER.error("System error", e);
            }
        }
        JOptionPane.showMessageDialog(instance,
                "Please select the last semester to be romoved",
                "Selection error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void createSemester() {
        SemesterManager sm = SemesterManager.getInstance();
        final List<Semester> allSemesterOfStudent = sm.getAllSemesterOfStudent(StudentManager.getInstance().getStudent());
        final int number = allSemesterOfStudent.size() + 1;
        Semester s = new Semester();
        s.setUserid(StudentManager.getInstance().getStudent());
        s.setNumber(number);
        s.setExaminationStart("");
        s.setSemesterStart("");
        sm.insertSemester(s);
        updateSemesterAndModulTreeView();
    }

    private void initSemesterBoard() {
        try {
            Calendar c = new GregorianCalendar();
            Calendar c1 = new GregorianCalendar();
            final String semesterNo = instance.getSemesterLabel().getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            int semesterNr = Integer.parseInt(semesterNo.replace("Semester ", ""));
            Semester semester = SemesterManager.getInstance().getSemesterByNumber(semesterNr);
            if (semester != null) {
                String semesterStart = semester.getSemesterStart();
                if (!semesterStart.isEmpty()) {
                    c.setTime(sdf.parse(semesterStart));
                    instance.getSemesterStartDateJCalender().setCalendar(c);
                }
                String examinationStart = semester.getExaminationStart();
                if (!examinationStart.isEmpty()) {
                    c1.setTime(sdf.parse(examinationStart));
                    instance.getExaminationStartDateJCalender().setCalendar(c1);
                }
                if (semester.getSemesterstatus()) {
                    instance.getCurrentSemesterJLabel().setForeground(Color.BLUE);
                    instance.getCurrentSemesterJLabel().setText("Semester " + semester.getNumber());
                    instance.getActivateDeactivateSemesterButton().setText(ButtonConstants.DEACTIVATE.toString());
                } else {
                    instance.getCurrentSemesterJLabel().setForeground(Color.BLACK);
                    instance.getCurrentSemesterJLabel().setText("Semester " + semester.getNumber());
                    instance.getActivateDeactivateSemesterButton().setText(ButtonConstants.ACTIVATE.toString());
                }
            }
        } catch (NumberFormatException | ParseException e) {
            LOGGER.error("System error", e);
        }
    }

    private void initModuleBoard(String moduleName) {
        if (moduleName == null) {
            moduleName = instance.getSelectedModuleLabel().getText();
        }

        final ModulManager moduleManager = ModulManager.getInstance();
        Modul theModul = moduleManager.getModulByName(moduleName);
        if (theModul != null) {
            if (theModul.getModulestatus()) {
                instance.getSelectedModuleLabel().setForeground(Color.BLUE);
                instance.getActivateDeactivateModuleButton().setText(ButtonConstants.DEACTIVATE.toString());
            } else {
                instance.getSelectedModuleLabel().setForeground(Color.BLACK);
                instance.getActivateDeactivateModuleButton().setText(ButtonConstants.ACTIVATE.toString());
            }
        }
    }

    public void setModuleAndSemestertrOverviewJtree(TreePath selectionPath) {
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        if (lastPathComponent.getParent() != null) {
            if (lastPathComponent.getParent().getParent() == null) {
                //Semester
                instance.getSemesterBoardPanel().setVisible(true);
                instance.getModuleBoardPanel().setVisible(false);
                instance.getCurrentSemesterJLabel().setText(String.valueOf(lastPathComponent.getUserObject()));
                instance.getSemesterLabel().setText(String.valueOf(lastPathComponent.getUserObject()));
                initSemesterBoard();
            } else {
                //Module
                actuailzeModuleBoardPanel(lastPathComponent);
            }
        }
    }

    private void actuailzeModuleBoardPanel(DefaultMutableTreeNode lastPathComponent) {
        instance.getModuleBoardPanel().setVisible(true);
        instance.getSemesterBoardPanel().setVisible(false);
        instance.getSelectedModuleLabel().setText(String.valueOf(lastPathComponent.getUserObject()));
        instance.getModulnameModulBPanel().setText(String.valueOf(lastPathComponent.getUserObject()));
        ModulManager moduleManager = ModulManager.getInstance();
        Modul module = moduleManager.getModulByName(String.valueOf(lastPathComponent.getUserObject()));
        String creditpoints = String.valueOf(module.getEctspunkte());
        String studyhours = String.valueOf(module.getStudyhours());
        instance.getCreditpointsModulBPanel().setText(creditpoints);
        instance.getStudyhoursModulBPanel().setText(studyhours);
        initModuleBoard(null);
        //check if the module has a record, else it should be create
        Student student = StudentManager.getInstance().getStudent();
        if (AcademicrecordsManager.getInstance().getAllRecords(student, module).isEmpty()) {
            Academicrecords newTry = new Academicrecords();
            newTry.setId(0);
            newTry.setStudentid(student);
            newTry.setModuleid(module);
            AcademicrecordsManager.getInstance().insertTry(newTry);
        }
        updateRecordsTable();
    }

    public void editModule() {
        if (!instance.getModulnameModulBPanel().getText().isEmpty()) {
            ModulManager moduleManager = ModulManager.getInstance();
            TreePath selectionPath = instance.getModulAndSemesterOverviewJTree().getSelectionPath();
            Modul module = moduleManager.getModulByName(String.valueOf(((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject()));
            try {
                if (module != null) {
                    module.setModulname(instance.getModulnameModulBPanel().getText());
                    module.setEctspunkte(Integer.parseInt(instance.getCreditpointsModulBPanel().getText()));
                    module.setStudyhours(Double.parseDouble(instance.getStudyhoursModulBPanel().getText()));
                    moduleManager.updateModul(module);
                    expandAllNodes(instance.getModulAndSemesterOverviewJTree());
                    updateSemesterAndModulTreeView();
                    organiserController.init();
                    instance.getSemesterBoardPanel().setVisible(false);
                    instance.getModuleBoardPanel().setVisible(false);
                    JOptionPane.showMessageDialog(instance,
                            "Module is successfully edited",
                            "Message",
                            JOptionPane.PLAIN_MESSAGE);
                }
            } catch (NumberFormatException | HeadlessException e) {
                LOGGER.error("System error", e);
                JOptionPane.showMessageDialog(instance,
                        "Check your input - Credit points and Study Hours have to be nummeric.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deleteModule() {
        if (!instance.getModulnameModulBPanel().getText().isEmpty()) {
            ModulManager modulmanager = ModulManager.getInstance();
            List<Modul> moduleList = modulmanager.getAllModule();
            for (Modul module : moduleList) {
                try {
                    if (module.getModulname().equals(instance.getModulnameModulBPanel().getText())) {
                        Object[] options = {"Yes, please", "No way!"};
                        int n = (Integer) JOptionPane.showOptionDialog(instance,
                                "Are you sure? Delete the module?",
                                "Warning!",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (n == JOptionPane.YES_OPTION) {
                            modulmanager.deleteModul(module);
                        } else if (n == JOptionPane.NO_OPTION) {
                            instance.getModuleBoardPanel().setVisible(true);
                            instance.getSemesterBoardPanel().setVisible(false);
                            instance.getModulnameModulBPanel().setText("");
                            instance.getCreditpointsModulBPanel().setText("");
                            instance.getStudyhoursModulBPanel().setText("");
                            return;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("System error", e);
                }
                expandAllNodes(instance.getModulAndSemesterOverviewJTree());
                updateSemesterAndModulTreeView();
                organiserController.init();
            }
            instance.getSelectedModuleLabel().setText("");
            instance.getModuleBoardPanel().setVisible(true);
            instance.getSemesterBoardPanel().setVisible(false);
            instance.getModulnameModulBPanel().setText("");
            instance.getCreditpointsModulBPanel().setText("");
            instance.getStudyhoursModulBPanel().setText("");
        }
        JOptionPane.showMessageDialog(instance,
                "Module is successfully deleted",
                "Message",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void createTryRow() {
        final String moduleName = instance.getSelectedModuleLabel().getText();
        Modul module = ModulManager.getInstance().getModulByName(moduleName);
        Student student = StudentManager.getInstance().getStudent();
        Academicrecords newTry = new Academicrecords();
        newTry.setId(0);
        newTry.setStudentid(student);
        newTry.setModuleid(module);
        AcademicrecordsManager.getInstance().insertTry(newTry);
        updateRecordsTable();
    }

    private void updateRecordsTable() {
        final String moduleName = instance.getSelectedModuleLabel().getText();
        Modul module = ModulManager.getInstance().getModulByName(moduleName);
        Student student = StudentManager.getInstance().getStudent();
        DefaultTableModel model = (DefaultTableModel) instance.getAcademicRecordsJTable().getModel();
        GuiServices.deleteTableContent(instance.getAcademicRecordsJTable());
        int number = 1;
        for (Academicrecords record : AcademicrecordsManager.getInstance().getAllRecords(student, module)) {
            model.addRow(new Object[]{number++, record.getExaminationdate(), record.getGrade()});
        }
    }

    public void removeTryRow() {
        Object[] options = {"Yes, please", "No way!"};
        int result = (Integer) JOptionPane.showOptionDialog(instance,
                "Do you want to delete the selected record?",
                "Warning!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == JOptionPane.YES_OPTION) {
            int rowNumber = instance.getAcademicRecordsJTable().getSelectedRow();
            final String moduleName = instance.getSelectedModuleLabel().getText();
            Modul module = ModulManager.getInstance().getModulByName(moduleName);
            Student student = StudentManager.getInstance().getStudent();
            List<Academicrecords> allRecords = AcademicrecordsManager.getInstance().getAllRecords(student, module);
            Academicrecords record = allRecords.get(rowNumber);
            AcademicrecordsManager.getInstance().removeRecord(record);
            updateRecordsTable();
        }
    }

    public void realizeTryDetailEntering() {
        int column = instance.getAcademicRecordsJTable().getSelectedColumn();
        int row = instance.getAcademicRecordsJTable().getSelectedRow();
        final String moduleName = instance.getSelectedModuleLabel().getText();
        Modul module = ModulManager.getInstance().getModulByName(moduleName);
        Student student = StudentManager.getInstance().getStudent();
        switch (column) {
            //examination date
            case 1: {
                String result = getExaminationDate();
                if (result != null) {
                    List<Academicrecords> allRecords = AcademicrecordsManager.getInstance().getAllRecords(student, module);
                    Academicrecords record = allRecords.get(row);
                    record.setExaminationdate(result);
                    AcademicrecordsManager.getInstance().insertTry(record);
                    updateRecordsTable();
                }
                break;
            }
            //grade
            case 2: {
                final String result = getGradePopup();
                if (result != null) {
                    List<Academicrecords> allRecords = AcademicrecordsManager.getInstance().getAllRecords(student, module);
                    Academicrecords record = allRecords.get(row);
                    record.setGrade(Double.parseDouble(result));
                    AcademicrecordsManager.getInstance().insertTry(record);
                    updateRecordsTable();
                }
            }
            default: {
            }
        }
    }

    private String getExaminationDate() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        JDateChooser dateChooser = new JDateChooser();
        panel.add(dateChooser);
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter your examination date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    return String.valueOf(sdf.format(dateChooser.getCalendar().getTime()));
                } catch (Exception e) {
                    LOGGER.error("System error", e);
                    return null;
                }
        }
        return null;
    }

    private String getGradePopup() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("0.0");
        model.addElement("1.0");
        model.addElement("1.3");
        model.addElement("1.7");
        model.addElement("2.0");
        model.addElement("2.3");
        model.addElement("2.7");
        model.addElement("3.0");
        model.addElement("3.3");
        model.addElement("3.7");
        model.addElement("4.0");
        model.addElement("4.3");
        model.addElement("4.7");
        model.addElement("5.0");
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter your grade", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                return String.valueOf(comboBox.getSelectedItem());
        }
        return null;
    }

}
