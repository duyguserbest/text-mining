package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;
import org.apache.mahout.math.Vector;

import java.util.List;
import java.util.Map;

public class MeanSquaredErrorCalculator {

    public static double calculate(Map<Integer, List<Integer>> predictions, List<Vector> centroids, Dataset dataset) {
        return predictions.entrySet().parallelStream()
                .mapToDouble(cluster -> calculateSquaredDistanceSum(centroids, dataset, cluster)).sum();
    }

    private static double calculateSquaredDistanceSum(List<Vector> centroids, Dataset dataset, Map.Entry<Integer, List<Integer>> cluster) {
        Vector centroid = getCentroid(centroids, cluster);
        return cluster.getValue().stream()
                .mapToDouble(docIndex -> calculateDistanceSquaredToCentroid(centroid, getDocumentVector(dataset, docIndex))).sum();
    }

    private static double calculateDistanceSquaredToCentroid(Vector centroid, Vector docVector) {
        return centroid.getDistanceSquared(docVector);
    }

    private static Vector getDocumentVector(Dataset dataset, Integer docIndex) {
        return dataset.getMatrix().viewRow(docIndex);
    }

    private static Vector getCentroid(List<Vector> centroids, Map.Entry<Integer, List<Integer>> cluster) {
        return centroids.get(cluster.getKey());
    }
}
