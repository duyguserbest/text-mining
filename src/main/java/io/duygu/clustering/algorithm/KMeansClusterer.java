package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;

import java.util.ArrayList;
import java.util.Arrays;
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
        int[] centroidIndexes = selectInitialCentroids(clusterCount);
        Arrays.stream(centroidIndexes).map(centroidIndex -> calculateSimilarity((int)centroidIndex)).collect(Collectors.toList());

    }

    private double[] calculateSimilarity(int centroidIndex) {
        return IntStream.range(0, dataset.getDataset().size()).asDoubleStream()
                .map(index -> cosineSimilarity.calculate(centroidIndex, (int) index)).toArray();
    }

    private int[] selectInitialCentroids(int clusterCount) {
        return IntStream.range(0, clusterCount).map(index -> random.nextInt(dataset.getMatrix().rowSize())).toArray();
    }


}
