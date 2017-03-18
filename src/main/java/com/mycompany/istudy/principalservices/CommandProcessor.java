package com.mycompany.istudy.principalservices;

import org.apache.log4j.Logger;

/**
 * Created by Varuni on 15.12.2016.
 */
public class CommandProcessor {

    private final static Logger logger = Logger.getLogger(CommandProcessor.class);

    public static void process(String command) throws Exception {
        //Create processor to run command
        Process process = Runtime.getRuntime().exec(command);
    }
}
