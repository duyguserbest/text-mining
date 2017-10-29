package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KMeansClusterer {

    private Random random;

    private Dataset dataset;

    private int iteration;

    private List<Vector> centroids;

    public KMeansClusterer(Dataset dataset) {
        this.random = new Random();
        this.dataset = dataset;
    }

    public Map<Integer, List<Integer>> cluster(int clusterCount, int iterationCount) {
        List<Integer> predictions = null;
        for (int iteration = 0; iteration < iterationCount; iteration++) {
            this.iteration = iteration;
            selectCentroids(predictions, clusterCount);
            List<List<Double>> datasetCentroidSimilarityValues = centroids.stream().map(this::calculateSimilarity)
                    .collect(Collectors.toList());

            long start = System.currentTimeMillis();
            predictions = predict(datasetCentroidSimilarityValues);
            long completionTime = System.currentTimeMillis() - start;
            System.out.println("iteration " + iteration + " predictions took: " + completionTime / 1000 + " seconds.");
        }
        return convertPredictionsToMap(predictions);
    }

    private Map<Integer, List<Integer>> convertPredictionsToMap(List<Integer> predictions) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < predictions.size(); i++) {
            List<Integer> docs = map.computeIfAbsent(predictions.get(i), k -> new ArrayList<>());
            docs.add(i);
        }
        return map;
    }

    private List<Integer> predict(List<List<Double>> datasetCentroidSimilarityValues) {
        return IntStream.range(0, dataset.getDataset().size())
                .mapToObj(index -> findMostSimiliarCentroid(datasetCentroidSimilarityValues, index))
                .collect(Collectors.toList());
    }

    protected int findMostSimiliarCentroid(List<List<Double>> datasetCentroidSimilarityValues, int index) {
        return IntStream.range(0, datasetCentroidSimilarityValues.size())
                .reduce((i, j) -> getMostSimiliarCentroidIndex(datasetCentroidSimilarityValues, index, i, j))
                .getAsInt();
    }

    private int getMostSimiliarCentroidIndex(List<List<Double>> datasetCentroidSimilarityValues, int index, int i, int j) {
        return datasetCentroidSimilarityValues.get(i).get(index) > datasetCentroidSimilarityValues.get(j).get(index) ? i : j;
    }

    private List<Double> calculateSimilarity(Vector centroid) {
        return IntStream.range(0, dataset.getDataset().size()).asDoubleStream()
                .mapToObj(index -> CosineSimilarityCalculator.calculate(centroid, dataset.getMatrix().viewRow((int) index)))
                .collect(Collectors.toList());
    }

    private void selectCentroids(List<Integer> predictions, int clusterCount) {
        if (predictions == null) {
            selectInitialCentroids(clusterCount);
        } else {
            calculateCentroids(predictions);
        }
    }

    private void calculateCentroids(List<Integer> predictions) {
        long start = System.currentTimeMillis();
        centroids.clear();
        Map<Integer, List<Integer>> clusters = convertPredictionsToMap(predictions);
        clusters.entrySet().parallelStream().forEach(cluster -> {
            Vector centroid = new SequentialAccessSparseVector(dataset.getVocabulary().size());
            long startsum = System.currentTimeMillis();
            for (Integer docVectorIndex : cluster.getValue()) {
                centroid = centroid.plus(dataset.getMatrix().viewRow(docVectorIndex));
            }
            long completionTime = System.currentTimeMillis() - startsum;
            System.out.println("iteration " + iteration + " centroid " + cluster.getKey() + " sum of " + cluster.getValue().size() + " vectors took: " + completionTime / 1000 + " seconds.");
            centroid = centroid.divide(cluster.getValue().size());
            centroids.add(centroid);
        });
        long completionTime = System.currentTimeMillis() - start;
        System.out.println("calculateCentroids took: " + completionTime / 1000 + " seconds.");
    }

    private void selectInitialCentroids(int clusterCount) {
        centroids = IntStream.range(0, clusterCount)
                .mapToObj(index -> dataset.getMatrix().viewRow(getRandomVectorIndex())).collect(Collectors.toList());
    }

    private int getRandomVectorIndex() {
        return random.nextInt(dataset.getMatrix().rowSize());
    }

    public List<Vector> getCentroids() {
        return centroids;
    }
}
