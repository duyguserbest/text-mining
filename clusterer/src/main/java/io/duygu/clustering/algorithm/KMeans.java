package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;
import io.duygu.logger.ElapsedTimeLogger;
import io.duygu.preprocess.OperationType;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class KMeans implements Clusterer{

    private Random random;

    private Dataset dataset;

    private int iteration;

    private List<Vector> centroids;

    private Map<Integer, List<Integer>> predictions;

    public KMeans(Dataset dataset) {
        this.random = new Random();
        this.dataset = dataset;
    }

    public Map<Integer, List<Integer>> cluster(int clusterCount, int iterationCount) {
        for (iteration = 0; iteration < iterationCount; iteration++) {
            ElapsedTimeLogger.log(OperationType.CENTROIDS, () -> selectCentroids(clusterCount));
            List<List<Double>> datasetCentroidSimilarityValues = centroids.stream().map(this::calculateSimilarity)
                                                                          .collect(Collectors.toList());
            predictions = ElapsedTimeLogger
                .log(OperationType.CLUSTERING_ITERATION, () -> predict(datasetCentroidSimilarityValues), iteration);
        }
        return predictions;
    }

    private Map<Integer, List<Integer>> predict(List<List<Double>> datasetCentroidSimilarityValues) {
        return IntStream.range(0, dataset.getDataset().size())
                        .mapToObj(index -> new AbstractMap.SimpleEntry<Integer, Integer>(index,
                            findMostSimiliarCentroid(datasetCentroidSimilarityValues, index)))
                        .collect(groupingBy(Map.Entry::getValue,
                            Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    private int findMostSimiliarCentroid(List<List<Double>> datasetCentroidSimilarityValues, int index) {
        return IntStream.range(0, datasetCentroidSimilarityValues.size())
                        .reduce((i, j) -> getMostSimiliarCentroidIndex(datasetCentroidSimilarityValues, index, i, j))
                        .getAsInt();
    }

    private int getMostSimiliarCentroidIndex(List<List<Double>> datasetCentroidSimilarityValues, int index, int i, int j) {
        return datasetCentroidSimilarityValues.get(i).get(index) > datasetCentroidSimilarityValues.get(j).get(index) ?
            i : j;
    }

    private List<Double> calculateSimilarity(Vector centroid) {
        return IntStream.range(0, dataset.getDataset().size()).asDoubleStream()
                        .mapToObj(index -> CosineSimilarityCalculator
                            .calculate(centroid, dataset.getMatrix().viewRow((int) index)))
                        .collect(Collectors.toList());
    }

    private void selectCentroids(int clusterCount) {
        if (iteration == 0) {
            selectInitialCentroids(clusterCount);
        } else {
            calculateCentroids();
        }
    }

    private void calculateCentroids() {
        centroids.clear();
        centroids = predictions.entrySet().parallelStream().map(cluster ->
            ElapsedTimeLogger
                .log(OperationType.CENTROID, () -> calculateCentroid(cluster), cluster.getKey(), iteration,
                    cluster.getValue().size())
        ).collect(Collectors.toList());
    }

    private Vector calculateCentroid(Map.Entry<Integer, List<Integer>> cluster) {
        Vector centroid = new SequentialAccessSparseVector(dataset.getVocabulary().size());
        for (Integer docVectorIndex : cluster.getValue()) {
            centroid = centroid.plus(dataset.getMatrix().viewRow(docVectorIndex));
        }
        centroid = centroid.divide(cluster.getValue().size());
        return centroid;
    }

    private void selectInitialCentroids(int clusterCount) {
        centroids = IntStream.range(0, clusterCount)
                             .mapToObj(index -> dataset.getMatrix().viewRow(getRandomVectorIndex()))
                             .collect(Collectors.toList());
    }

    private int getRandomVectorIndex() {
        return random.nextInt(dataset.getMatrix().rowSize());
    }

    public double calculateErrorRate() {
        return MeanSquaredErrorCalculator.calculate(predictions, centroids, dataset);
    }
}
