package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KMeansClusterer {

    private Random random;

    private Dataset dataset;

    private CosineSimilarityCalculator cosineSimilarity;

    public KMeansClusterer(Dataset dataset) {
        this.random = new Random();
        this.dataset = dataset;
        this.cosineSimilarity = new CosineSimilarityCalculator(dataset);
    }

    public void cluster(int clusterCount, int iterationCount) {
        List<Integer> centroidIndexes = selectInitialCentroids(clusterCount);
        List<List<Double>> datasetCentroidSimilarityValues = centroidIndexes.stream().map(this::calculateSimilarity).collect(Collectors.toList());
        predict(datasetCentroidSimilarityValues);
    }

    private void predict(List<List<Double>> datasetCentroidSimilarityValues) {
        List<Double> predictions = IntStream.range(0, dataset.getDataset().size()).asDoubleStream().map(index -> findMostSimiliarCentroid(datasetCentroidSimilarityValues, (int) index)).boxed().collect(Collectors.toList());
    }

    private double findMostSimiliarCentroid(List<List<Double>> datasetCentroidSimilarityValues, int index) {
        return datasetCentroidSimilarityValues.stream()
                .max(Comparator.comparingDouble(similarityToCentroid -> similarityToCentroid.get(index))).get().get(index);
        //TODO this method returns max cosine similarity value however the value necessary for the calculation is index of the list (centroid) that contains the max value.
    }

    private List<Double> calculateSimilarity(int centroidIndex) {
        return IntStream.range(0, dataset.getDataset().size()).asDoubleStream()
                .map(index -> cosineSimilarity.calculate(centroidIndex, (int) index)).boxed().collect(Collectors.toList());
    }

    private List<Integer> selectInitialCentroids(int clusterCount) {
        return IntStream.range(0, clusterCount).map(index -> getRandomVectorIndex()).boxed().collect(Collectors.toList());
    }

    private int getRandomVectorIndex() {
        return random.nextInt(dataset.getMatrix().rowSize());
    }


}
