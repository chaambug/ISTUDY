package com.mycompany.istudy.principalservices;

import com.mycompany.istudy.principalservices.Intf.IStudyDefaultJFreeChartServiceIntf;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.JFreeChart;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;

/**
 * Implements the IStudyDefaultJFreeChartService Interface
 * @author Cham
 */

public class IStudyDefaultJFreeService implements IStudyDefaultJFreeChartServiceIntf {

    private final static Logger LOGGER = Logger.getLogger(IStudyDefaultJFreeService.class);

    private final double width;
    private final double height;

    public IStudyDefaultJFreeService() {
        //Default setting
        width = 640;
        height = 480;
    }

    public IStudyDefaultJFreeService(double width, double height) {
        //Default setting
        this.width = width;
        this.height = height;
    }

    /**
     * This method generates a 2D line chart with svg format
     * @param filePath file (Ex. D:\\myLineChart.svg)
     * @param descTop
     * @param descX
     * @param descY
     * @param investedPoints xy-coordinates
     * @param expectedPoints
     * @return
     */
    @Override
    public boolean create2DLineChart(
            String filePath,
            String descTop,
            String descX,
            String descY,
            Map<String, String> investedPoints,
            Map<String, String> expectedPoints) {

        LOGGER.info("service call create2DLineChart");
        try {
            double xPoint;
            double yPoint;

            //Invested Performance
            XYSeries investedData = new XYSeries("Invested");
            for (String key : investedPoints.keySet()) {
                xPoint = Double.valueOf(key);
                yPoint = Double.valueOf(investedPoints.get(key));
                investedData.add(xPoint, yPoint);
            }

            //Expected performance
            XYSeries expectedData = new XYSeries("Expected");
            for (String key : expectedPoints.keySet()) {
                xPoint = Double.valueOf(key);
                yPoint = Double.valueOf(expectedPoints.get(key));
                expectedData.add(xPoint, yPoint);
            }

            //XYSeriesCollection implements XYDataset
            XYSeriesCollection svgXYDataSeries = new XYSeriesCollection();

            // add series using addSeries method
            svgXYDataSeries.addSeries(investedData);
            svgXYDataSeries.addSeries(expectedData);

            //Use createXYLineChart to create the chart
            JFreeChart XYLineChart = ChartFactory.createXYLineChart(descTop, 
                    descX, descY, svgXYDataSeries, PlotOrientation.VERTICAL, 
                    true, true, false);
            ChartUtilities.saveChartAsPNG(new File(filePath), XYLineChart, 
                    (int) width, (int) height);

            return true;
        } catch (IOException | NumberFormatException i) {
            LOGGER.error("System error", i);
            return false;
        }
    }
}
