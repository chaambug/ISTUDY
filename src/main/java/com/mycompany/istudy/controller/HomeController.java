/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.entities.Userloginentries;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.db.services.impl.UserLoginEntriesManager;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JTextArea;
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
        //active semester and modules
        GuiServices.deleteNotificationTextAreaContent(instance.getnotificationTextArea());
        
        try {
            if(totalDaysToExam()== 28){
                addToNotificationWindow(totalDaysToExam() + " Days to the examination period!Be aware that you are in takt!");
            }else if (totalDaysToExam()< 28){
                addToNotificationWindow(totalDaysToExam() + " Days to the examination period!\n Please check whether you are performing well with all the modules");
            }else {
                addToNotificationWindow(totalDaysToExam() + " Days to examination period!\n PLease make sure that you are covering all the choosen modules!");
            }
        } catch (Exception ex) {
            LOGGER.error("System error", ex);
        }
    }
    
    public void addToNotificationWindow(String text){
        JTextArea notificationTExtArea = instance.getnotificationTextArea();
        notificationTExtArea.append("\n"+text);
        
    }
    
    public int totalDaysToExam() throws Exception{
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
}
