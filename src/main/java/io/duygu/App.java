package io.duygu;

import io.duygu.clustering.algorithm.KMeansClusterer;
import io.duygu.clustering.algorithm.MeanSquaredErrorCalculator;
import io.duygu.clustering.chart.LineChartDrawer;
import io.duygu.dto.Dataset;
import io.duygu.dto.Thesis;
import io.duygu.preprocess.DatasetBuilder;
import io.duygu.preprocess.DatasetPreprocessor;
import io.duygu.preprocess.DatasetReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.exit(1);
        }

//        JCommander jCommander = new JCommander();
//        CommanDLineArguments

        DatasetReader reader = new DatasetReader();
        List<Thesis> thesisList = reader.getDocs(args[0]);
        DatasetPreprocessor preprocessor = new DatasetPreprocessor();
        List<Thesis> theses = preprocessor.process(thesisList);
        Dataset data = new DatasetBuilder().createDataset(theses);
        KMeansClusterer clusterer = new KMeansClusterer(data);
        Map<Integer, Double> clusterErrorRate = new HashMap<>();
        for (int clusterCount = 2; clusterCount <= 20; clusterCount++) {
            long start = System.currentTimeMillis();
            Map<Integer, List<Integer>> predictions = clusterer.cluster(clusterCount, 3);
            long completionTime = System.currentTimeMillis() - start;
            System.out.println("Clustering took: " + completionTime / 1000 + " seconds.");
            double error = MeanSquaredErrorCalculator.calculate(predictions, clusterer.getCentroids(), data);
            clusterErrorRate.put(clusterCount, error);
        }
        try {
            LineChartDrawer.draw(clusterErrorRate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}