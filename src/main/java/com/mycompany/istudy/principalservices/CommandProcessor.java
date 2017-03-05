package com.mycompany.istudy.principalservices;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Varuni on 15.12.2016.
 */
public class CommandProcessor {

    private final static Logger logger = Logger.getLogger(CommandProcessor.class);

    public static void process(String command) throws Exception {
        //Create processor to run command
        Process process = Runtime.getRuntime().exec(command);
    }

    public static void processAndWrite(String command, String path) throws IOException {
        //Create processor to run command
        String[] commandd = {"cmd", "/c", command};
        Process process = Runtime.getRuntime().exec(commandd);
        System.out.println("the output stream is " + process.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
        }
    }
}
