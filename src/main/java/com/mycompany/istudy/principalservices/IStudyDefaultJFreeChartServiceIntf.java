package com.mycompany.istudy.principalservices;

import java.util.Map;


public interface IStudyDefaultJFreeChartServiceIntf {

    public boolean create2DLineChart(
            String filePath,
            String descTop,
            String descX,
            String descY,
            Map<String, String> investedPoints,
            Map<String, String> expectedPoints);
}
