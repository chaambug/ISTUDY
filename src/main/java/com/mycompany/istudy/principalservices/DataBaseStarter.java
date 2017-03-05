package com.mycompany.istudy.principalservices;

import org.apache.log4j.Logger;

import java.util.Properties;
import javax.swing.JTextArea;

/**
 * Created by Varuni on 15.12.2016.
 */
public class DataBaseStarter extends Thread{

    private final static Logger logger = Logger.getLogger(DataBaseStarter.class);
    private final JTextArea textArea;
    
    public DataBaseStarter(JTextArea report) {
        textArea = report;
    }
    
    private void startDB() {
        try {
            //Read file name and path of xampp home directory
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("system.properties"));
            final String xampp_home_dir = props.getProperty("xampp.dir");
            final String mysql_start_file = props.getProperty("mysql.file");

            //create command
            String command = String.format("%s/%s", xampp_home_dir, mysql_start_file);

            //run command
            CommandProcessor.process(command);
            textArea.append(">> Database started successfully\n");
        } catch (Exception e) {
            logger.error("Database could not be started. Check for installation of mysql.", e);
            textArea.append(">> Database could not be started (check system log)\n");
        }
    }

    @Override
    public void run() {
        logger.info(String.format("Thread started..."));
        startDB();
    }
}
