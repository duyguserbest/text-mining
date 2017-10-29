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

    public static void draw(Map<Integer, Double> clusterErrorRate) throws IOException {
        DefaultCategoryDataset dataset = createDataset(clusterErrorRate);
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                CHART_TITLE, X_AXIS_TITLE, Y_AXIS_TITLE, dataset, PlotOrientation.VERTICAL,
                true, true, false);

        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */
        File lineChart = new File("Clustering.jpeg");
        ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, width, height);
    }

    private static DefaultCategoryDataset createDataset(Map<Integer, Double> clusterErrorRate) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        clusterErrorRate.entrySet().stream().forEach(entry -> dataset.addValue(entry.getValue(), Y_AXIS_TITLE, entry.getKey()));
        return dataset;
    }
}
