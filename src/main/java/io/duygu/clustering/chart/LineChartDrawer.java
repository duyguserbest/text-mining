package io.duygu.clustering.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LineChartDrawer {

    public static final String CHART_TITLE = "Turkish Thesis Clustering";

    public static final String X_AXIS_TITLE = "Cluster Count";

    public static final String Y_AXIS_TITLE = "Squared Error";

    public static final int WIDTH = 640;

    public static final int HEIGHT = 480;

    public static final String CHART_JPG_FILE_NAME = "Clustering.jpeg";

    public static void draw(Map<Integer, Double> clusterErrorRate) throws IOException {
        DefaultCategoryDataset dataset = createDataset(clusterErrorRate);
        JFreeChart lineChartObject = ChartFactory.createLineChart(CHART_TITLE, X_AXIS_TITLE, Y_AXIS_TITLE, dataset,
            PlotOrientation.VERTICAL, true, true, false);
        File lineChart = new File(CHART_JPG_FILE_NAME);
        ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, WIDTH, HEIGHT);
    }

    private static DefaultCategoryDataset createDataset(Map<Integer, Double> clusterErrorRate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        clusterErrorRate.entrySet().forEach(entry -> dataset.addValue((entry.getValue()- 25000d), Y_AXIS_TITLE, entry.getKey()));
        return dataset;
    }
}
