package com.mycompany.istudy.principalservices;

import org.apache.log4j.Logger;

import java.util.Properties;
import javax.swing.JTextArea;

/**
 * This class starts the xampp database server in a thread.
 * Created by Cham on 15.12.2016.
 */
public class DataBaseStarter extends Thread{

    private final static Logger LOGGER = Logger.getLogger(DataBaseStarter.class);
    private final JTextArea textArea;
    
    public DataBaseStarter(JTextArea report) {
        textArea = report;
    }
    
    private void startDB() {
        try {
            //Read file name and path of xampp home directory
            IstudyProperties istudyProperties = new IstudyProperties();
            Properties props = istudyProperties.getConfig();
            final String xampp_home_dir = props.getProperty(istudyProperties.XAMPP_DIR);
            final String mysql_start_file = props.getProperty(istudyProperties.MYSQL_FILE);

            //create command
            String command = String.format("%s/%s", xampp_home_dir, mysql_start_file);

            //run command
            CommandProcessor.process(command);
            textArea.append(">> Database started successfully\n");
        } catch (Exception e) {
            LOGGER.error("Database could not be started. Check for installation of mysql.", e);
            textArea.append(">> Database could not be started (check system log)\n");
        }
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Thread started..."));
        startDB();
    }
}
