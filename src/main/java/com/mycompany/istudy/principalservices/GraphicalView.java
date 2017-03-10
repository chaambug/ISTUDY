/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.principalservices;
/**
 *
 * @author Varuni
 */
import java.awt.Color; 
import java.awt.BasicStroke; 
import java.util.Map;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class GraphicalView extends ApplicationFrame {
    
    private final JFreeChart xylineChart;
    
    public GraphicalView(String applicationTitle, String chartTitle, Map<Double,Double> investedHoursPerWeek, Map<Double,Double> hoursToBeInvested) {
        super(applicationTitle);
        xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Calender Weeks",
                "Invested Study-hours",
                createDataset(investedHoursPerWeek, hoursToBeInvested),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        final XYPlot plot = xylineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(Map<Double,Double> investedHoursPerWeek, Map<Double,Double> hoursToBeInvested){
        final XYSeries moduleToWeek = new XYSeries("Actual Performance");
        
        investedHoursPerWeek.entrySet().stream().forEach((pair) -> {
            moduleToWeek.add((double) pair.getKey(), (double) pair.getValue());
        });
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(moduleToWeek);
        
        final XYSeries optimalWorkload = new XYSeries("Optimal Performance ");
        
        hoursToBeInvested.entrySet().stream().forEach((pair) -> {
            optimalWorkload.add((double) pair.getKey(), (double) pair.getValue());
        });
        dataset.addSeries(optimalWorkload);
        return dataset;
    }  
}
