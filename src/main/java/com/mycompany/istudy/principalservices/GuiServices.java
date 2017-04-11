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
 * Class for services used by the UI.
 * @author Cham
 */
public class GuiServices {
    
     /**
     * Deletes table content of given table.
     * @param obj table of which the content will be deleted.
     */
    public static void deleteTableContent(JTable obj) {
        DefaultTableModel model = (DefaultTableModel) obj.getModel();
        for (int i = (model.getRowCount() - 1); i >= 0; i--) {
            model.removeRow(i);
        }
    }
    
    /**
     * Gives the number of calendar weeks between two dates.
     * @param d1 first date.
     * @param d2 second date.
     * @return number of weeks between these dates.
     * @throws Exception 
     */
    public static long getCalendarWeeks(Date d1, Date d2) throws Exception {
        final long millis = (d2.getTime() - d1.getTime());
        return TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS) / 7;
    }
    
    /**
     * Deletes the notification textarea in home window.
     * @param notificationTextArea 
     */
    public static void deleteNotificationTextAreaContent
        (JTextArea notificationTextArea) {
        notificationTextArea.setText(null);
    }
        
     /**
     * Gets the calendar days between two dates.
     * @param d1 first date.
     * @param d2 second date.
     * @return days between dates as long.
     * @throws Exception 
     */
    public static long getCalenderDays(Date d1, Date d2) throws Exception {
        final long millis = (d2.getTime() - d1.getTime());
        return TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
    }
}
