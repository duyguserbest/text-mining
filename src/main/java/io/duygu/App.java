package io.duygu;

import io.duygu.clustering.algorithm.KMeans;
import io.duygu.clustering.algorithm.MeanSquaredErrorCalculator;
import io.duygu.clustering.algorithm.PurityCalculator;
import io.duygu.clustering.chart.LineChartDrawer;
import io.duygu.dto.Dataset;
import io.duygu.dto.Thesis;
import io.duygu.logger.ElapsedTimeLogger;
import io.duygu.preprocess.DatasetBuilder;
import io.duygu.preprocess.DatasetPreprocessor;
import io.duygu.preprocess.DatasetReader;
import io.duygu.preprocess.OperationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.exit(1);
        }

//        JCommander jCommander = new JCommander();
//        CommanDLineArguments

        String filePath = args[0];
        List<Thesis> thesisList = ElapsedTimeLogger.log(OperationType.PARSE, () -> DatasetReader.getDocs(filePath));
        List<Thesis> theses = ElapsedTimeLogger
            .log(OperationType.ELIMINATION, () -> DatasetPreprocessor.eliminateNullAbstracts(thesisList));
        Dataset data = new DatasetBuilder().createDataset(theses);
        KMeans clusterer = new KMeans(data);
        Map<Integer, Double> clusterErrorRate = new HashMap<>();
        for (int clusterCount = 2; clusterCount <= 20; clusterCount++) {
            final int count = clusterCount;
            final Map<Integer, List<Integer>> predictions =
                ElapsedTimeLogger.log(OperationType.CLUSTERING, () -> clusterer.cluster(count, 3));
            double error = MeanSquaredErrorCalculator.calculate(predictions, clusterer.getCentroids(), data);
            clusterErrorRate.put(clusterCount, error);
            Double purity =
                ElapsedTimeLogger.log(OperationType.PURITY, () -> PurityCalculator.calculate(predictions, data));
            LOGGER.info("Purity for clusterCount " + clusterCount + ": " + purity);
        }
        LineChartDrawer.draw(clusterErrorRate);
    }

}