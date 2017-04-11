package com.mycompany.istudy.principalservices.Intf;

import java.util.Map;
/**
 * Interface for the IStudyDefaultJFreeService class
 * @author Cham
 */
public interface IStudyDefaultJFreeChartServiceIntf {

    public boolean create2DLineChart(
            String filePath,
            String descTop,
            String descX,
            String descY,
            Map<String, String> investedPoints,
            Map<String, String> expectedPoints);
}
