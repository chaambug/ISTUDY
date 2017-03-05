/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.principalservices;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Varuni
 */
public class GuiServices {
    public static void deleteTableContent(JTable obj) {
        DefaultTableModel model = (DefaultTableModel) obj.getModel();
        for (int i = (model.getRowCount() - 1); i >= 0; i--) {
            model.removeRow(i);
        }
    }
    
    public static long getCalendarWeeks(Date d1, Date d2) throws Exception {
        final long millis = (d2.getTime() - d1.getTime());
        return TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS) / 7;
    }

    public static void deleteNotificationTextAreaContent(JTextArea notificationTextArea) {
        notificationTextArea.setText(null);              
        
    }
    public static long getCalenderDays(Date d1, Date d2) throws Exception {
        final long millis = (d2.getTime() - d1.getTime());
        return TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
    }
}   
    

//if (month == 4 || month == 6 || month == 9 || month == 11)
//daysInMonth = 30;
//else 
//if (month == 2) 
//daysInMonth = (leapYear) ? 29 : 28;
//else 
//daysInMonth = 31;


