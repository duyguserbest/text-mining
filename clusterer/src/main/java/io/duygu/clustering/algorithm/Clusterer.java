package io.duygu.clustering.algorithm;

import java.util.List;
import java.util.Map;

public interface Clusterer {

    public Map<Integer, List<Integer>> cluster(int clusterCount, int iterationCount);

    public double calculateErrorRate();
}
