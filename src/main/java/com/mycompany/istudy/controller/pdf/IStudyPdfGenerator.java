/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller.pdf;

import com.mycompany.istudy.gui.UserWin;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * This class manages the generation of PDF files. The User can generate PDF files with an overview of his study performance. 
 * 
 * @author Chaam
 */
public abstract class IStudyPdfGenerator implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(IStudyPdfGenerator.class);

    protected UserWin instance;
    protected String outputDir;
    private Thread t;

    abstract void generate() throws Exception;
/**
 * Run method generates the PDF file in a Thread.
 */
    @Override
    public void run() {
        try {
            generate();
            if (instance != null) {
                JOptionPane.showMessageDialog(instance,
                        "PDF file is created.",
                        "Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            if (instance != null) {
                JOptionPane.showMessageDialog(instance,
                        "PDF file could not be created.",
                        "Not successful",
                        JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error("System error", e);
        }
    }
/**
 * Starts thread which generates PDF file.
 * @param instance Instance of IStudyPdfGenerator.
 */
    protected void startGenerating(IStudyPdfGenerator instance) {
        t = new Thread(instance, "iStudyPdfGeneratorThread");
        t.start();
        LOGGER.info("Thread : " + t.getName() + " is started to generate PDF");
    }

    public void waitForThread() throws InterruptedException {
        if (t.isAlive()) {
            t.join();
        }
    }
}
