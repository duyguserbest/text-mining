package io.duygu.clustering.algorithm;

import org.apache.mahout.math.Vector;

public class CosineSimilarityCalculator {

    public static double calculate(Vector vectorA, Vector vectorB) {
        return vectorA.dot(vectorB) / (vectorA.norm(2d) * vectorB.norm(2d));
    }
}
