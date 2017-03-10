/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.principalservices;

import java.awt.Color;
import java.awt.Component;
import java.util.GregorianCalendar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Varuni
 */
public class TableCalendarRenderer extends DefaultTableCellRenderer {
    public static int realYear, realMonth, realDay, currentYear, currentMonth;
    
    public  TableCalendarRenderer(){
    GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;    
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        if (column == 0 || column == 6) { //Week-end
            setBackground(new Color(255, 220, 220));
        } else { //Week
            setBackground(new Color(255, 255, 255));
        }
        if (value != null) {
            value = (value.toString()).substring(0, value.toString().indexOf(" "));
            if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear) { //Today
                setBackground(new Color(220, 220, 255));
            }
        }
        
        setBorder(null);
        setForeground(Color.black);
        return this;
    }
}
    

