package com.mycompany.istudy.db.services.intf;

import java.util.Map;


public interface IStudyDefaultJFreeSvgServiceIntf {

    public boolean create2DLineChart(
            String filePath,
            String descTop,
            String descX,
            String descY,
            Map<String, String> investedPoints,
            Map<String, String> expectedPoints);
}
