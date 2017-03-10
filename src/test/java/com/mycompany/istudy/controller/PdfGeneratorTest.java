/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.controller.pdf.PerformancePdfController;
import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.services.impl.StudentManager;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.*;
/**
 *
 * @author Varuni
 */
public class PdfGeneratorTest {
  
    private final static Logger LOGGER = Logger.getLogger(PdfGeneratorTest.class);
    
    PerformancePdfController pdf;
    
    @Before
    public void init() {
        List<Student> allStudents = StudentManager.getInstance().getAllStudents();
        StudentManager.getInstance().setStudent(allStudents.get(1));
        pdf = new PerformancePdfController();
    }
    
    @Test
    public void generatePdf() throws InterruptedException {
        LOGGER.info("TEST : generatePdf()");
        pdf.generatePDF(null, null);
        pdf.waitForThread();
    }  
    
}
