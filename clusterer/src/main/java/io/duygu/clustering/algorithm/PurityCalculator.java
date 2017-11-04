package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class PurityCalculator {

    private static Random random = new Random();

    public static double calculate(Map<Integer, List<Integer>> predictions, Dataset dataset) {
        List<Integer> clusters = getFourRandomInt(predictions.size());
        double trueCategorizedDocCount = clusters.stream().mapToDouble(cluster -> findMaxCategoryForCluster(predictions, dataset, cluster)).sum();
        double categorizedDocCount = clusters.stream().mapToDouble(cluster -> predictions.get(cluster).size()).sum();
        return (1 / categorizedDocCount) * trueCategorizedDocCount;
    }

    private static Long findMaxCategoryForCluster(Map<Integer, List<Integer>> predictions, Dataset dataset, Integer cluster) {
        return countCategories(predictions, dataset, cluster).entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
    }

    private static Map<String, Long> countCategories(Map<Integer, List<Integer>> predictions, Dataset dataset, Integer cluster) {
        return predictions.get(cluster).stream().map(docIndex -> dataset.getCategories().get(docIndex)).collect(groupingBy(Function.identity(), counting()));
    }

    private static List<Integer> getFourRandomInt(int max) {
        return IntStream.range(0, 4).mapToObj(operand -> random.nextInt(max)).collect(Collectors.toList());
    }
}
