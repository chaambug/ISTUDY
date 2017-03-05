package com.mycompany.istudy.controller;


import com.mycompany.istudy.db.services.impl.IStudyDefaultJFreeSvgService;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Varuni
 */
public class SvgGeneratorTest {
    
    private final static Logger logger = Logger.getLogger(SvgGeneratorTest.class);
    
    IStudyDefaultJFreeSvgService  iStudyDefaultJFreeSvgService;
    
    @Before
    public void init() {
       iStudyDefaultJFreeSvgService = new IStudyDefaultJFreeSvgService();
    }
    
    @Test
    public void generateSvg() {
        logger.info("TEST : generateSvg()");
        Map<String, String> invested = new HashMap<>();
        invested.put("0", "0");
        invested.put("1", "5");
        invested.put("2", "7");
        invested.put("3", "10");
        invested.put("4", "20");
        invested.put("5", "21");
        invested.put("6", "21");
        invested.put("7", "21");
        invested.put("8", "21");
        invested.put("9", "22");
        invested.put("10", "28");
        invested.put("11", "35");
        invested.put("12", "40");
        
        Map<String, String> expected = new HashMap<>();
        expected.put("0", "0");
        expected.put("1", "10");
        expected.put("2", "20");
        expected.put("3", "30");
        expected.put("4", "40");
        expected.put("5", "50");
        expected.put("6", "60");
        expected.put("7", "70");
        expected.put("8", "80");
        expected.put("9", "90");
        expected.put("10", "100");
        expected.put("11", "110");
        expected.put("12", "120");
        
        assertTrue(iStudyDefaultJFreeSvgService.create2DLineChart(
                System.getProperty("user.dir") + File.separator + "test.svg", 
                "Technische Informatik I",
                "Weeks", 
                "Hours",
                invested,
                expected));
    }  
}
