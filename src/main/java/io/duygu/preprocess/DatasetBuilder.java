package io.duygu.preprocess;

import io.duygu.dto.Dataset;
import io.duygu.dto.Thesis;
import io.duygu.logger.ElapsedTimeLogger;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.SparseRowMatrix;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class DatasetBuilder {

    private List<String[]> tokenizedDataset;

    private Dataset dataset;

    public Dataset createDataset(List<Thesis> theses) throws Exception {
        dataset = new Dataset(theses);
        tokenizedDataset = ElapsedTimeLogger.log(OperationType.TOKENIZATION, this::tokenizeDataset);
        dataset.setVocabulary(ElapsedTimeLogger.log(OperationType.VOCABULARY, this::createVocabulary));
        dataset.setCategories(ElapsedTimeLogger.log(OperationType.CATEGORY, this::parseCategory));
        dataset.setMatrix(ElapsedTimeLogger.log(OperationType.MATRIX, this::getSparseRowMatrix));
        return dataset;
    }

    private List<String[]> tokenizeDataset() {
        return dataset.getDataset().stream()
                      .map(this::tokenizeThesisAbstract)
                      .collect(Collectors.toList());
    }

    private List<String> createVocabulary() {
        return tokenizedDataset.stream()
                               .flatMap(Arrays::stream).distinct().sorted()
                               .collect(Collectors.toList());
    }

    private Map<String, Long> getTermAppearingDocCount() {
        return tokenizedDataset.stream().map(vector -> Arrays.stream(vector).collect(Collectors.toSet()))
                               .flatMap(Set::stream)
                               .collect(groupingBy(Function.identity(), counting()));
    }

    private Matrix getSparseRowMatrix() {
        Matrix matrix = new SparseRowMatrix(getTokenizedDataSetSize(), dataset.getVocabulary().size());
        Map<String, Long> termAppearingDocCount = ElapsedTimeLogger.log(OperationType.TERM, this::getTermAppearingDocCount);
        IntStream.range(0, getTokenizedDataSetSize())
                 .forEach(rowIndex -> matrix.assignRow(rowIndex, vectorizeDoc(rowIndex, termAppearingDocCount)));
        return matrix;
    }

    private SequentialAccessSparseVector vectorizeDoc(int doc, Map<String, Long> termAppearingDocCount) {
        SequentialAccessSparseVector vector = new SequentialAccessSparseVector(dataset.getVocabulary().size());
        Map<String, Long> termFrequencyInDoc = calculateTermFrequencyInDoc(doc);
        double docWordCount = termFrequencyInDoc.values().stream().mapToDouble(Long::doubleValue).sum();
        termFrequencyInDoc.forEach((word, frequency) -> {
            final int wordIndex = findWordIndex(word);
            final Double tfIdf = calculateTFIDF(frequency.doubleValue(), docWordCount, getTokenizedDataSetSize(),
                termAppearingDocCount.get(word).doubleValue());
            vector.set(wordIndex, tfIdf);
        });
        return vector;
    }

    private int findWordIndex(String word) {
        return Collections.binarySearch(dataset.getVocabulary(), word);
    }

    private Map<String, Long> calculateTermFrequencyInDoc(int doc) {
        return Arrays.stream(tokenizedDataset.get(doc)).collect(groupingBy(Function.identity(), counting()));
    }

    private int getTokenizedDataSetSize() {
        return tokenizedDataset.size();
    }

    private Double calculateTFIDF(double docTermAppearanceCount, double docWordCount, double docCount,
                                  double termAppearingDocCount) {
        double termFrequency = docTermAppearanceCount / docWordCount;
        double inverseDocumentFrequency = Math.log(docCount / termAppearingDocCount);
        return termFrequency * inverseDocumentFrequency;
    }

    private String[] tokenizeThesisAbstract(Thesis thesis) {
        return thesis.getTr().toLowerCase(new Locale("tr", "TR")).replaceAll("\\p{Punct}+", "").split("\\s+");
    }

    private List<String> parseCategory() {
        return dataset.getDataset().stream().map(this::parseFacultyName).collect(Collectors.toList());
    }

    private String parseFacultyName(Thesis thesis) {
        String categoryDirty = thesis.getMeta().split("\\n")[4];
        String[] split = categoryDirty.split("/");
        if (split.length > 2) {
            return split[1];
        } else {
            return categoryDirty;
        }
    }

}