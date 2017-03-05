/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.principalservices;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Varuni
 */
public class IstudyProperties {
    
    private static final String CONFIG = "config.properties";
    
    //Config keys
    public final String XAMPP_DIR = "xampp.dir";
    public final String MYSQL_FILE = "mysql.file";
    
    public final String INPUT_DIR = "inputDir";
    public final String CHARTS_DIR = "chartsDir";
    public final String XML_DIR = "xmlDir";
    public final String XML_FILE = "xmlFile";
    public final String PDF_OUTPUT_DIR = "pdfOutputDir";
    
    public Properties getConfig() throws Exception {
            ClassLoader classLoader = getClass().getClassLoader();
            Properties result = new Properties();
            InputStream is = classLoader.getResourceAsStream(CONFIG);
            result.load(is);
            return result;
    }
    
}
