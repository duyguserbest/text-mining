package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;

import java.util.stream.IntStream;

public class CosineSimilarityCalculator {

    private Dataset dataset;
    private double dotProduct = 0.0;
    private double normA = 0.0;
    private double normB = 0.0;

    public CosineSimilarityCalculator(Dataset dataset) {
        this.dataset = dataset;
    }

    public double calculate(int vectorAIndex, int vectorBIndex) {
        IntStream.range(0, dataset.getVocabulary().size())
                .forEach(index -> calculateSimilarityForColumn(vectorAIndex, vectorBIndex, index));
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private void calculateSimilarityForColumn(int vectorAIndex, int vectorBIndex, int columnIndex) {
        normA += calculatePartOfNorm(vectorAIndex, columnIndex);
        normB += calculatePartOfNorm(vectorBIndex, columnIndex);
        dotProduct += calculatePartOfDotProduct(vectorAIndex, vectorBIndex, columnIndex);
    }

    private double calculatePartOfDotProduct(int vectorAIndex, int vectorBIndex, int columnIndex) {
        return dataset.getMatrix().get(vectorAIndex, columnIndex) * dataset.getMatrix().get(vectorBIndex, columnIndex);
    }

    private double calculatePartOfNorm(int vectorIndex, int columnIndex) {
        return Math.pow(dataset.getMatrix().get(vectorIndex, columnIndex), 2);
    }


}
