package io.duygu.preprocess;

import io.duygu.dto.Dataset;
import io.duygu.dto.Thesis;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.SparseRowMatrix;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class DatasetBuilder {

    private List<String[]> tokenizedDataset;
    private Dataset dataset;

    public Dataset createDataset(List<Thesis> theses) throws FileNotFoundException {
        dataset = new Dataset(theses);

        calculate(OperationType.TOKENIZATION, () -> tokenizeDataset(theses));
        calculate(OperationType.VOCABULARY, this::createVocabulary);
        calculate(OperationType.TERM, this::getTermAppearingDocCount);
        calculate(OperationType.MATRIX, this::getSparseRowMatrix);

        return dataset;
    }

    private void tokenizeDataset(List<Thesis> dataset) {
        tokenizedDataset = dataset.stream()
                .map(this::tokenizeThesisAbstract)
                .collect(Collectors.toList());
    }

    private void createVocabulary() {
        dataset.setVocabulary(tokenizedDataset.stream()
                .flatMap(Arrays::stream).distinct().sorted()
                .collect(Collectors.toList()));
    }

    private Map<String, Long> getTermAppearingDocCount() {
        return tokenizedDataset.stream().map(vector -> Arrays.stream(vector).collect(Collectors.toSet())).flatMap(Set::stream)
                .collect(groupingBy(Function.identity(), counting()));
    }

    private void getSparseRowMatrix() {
        Matrix corpus = new SparseRowMatrix(getTokenizedDataSetSize(), dataset.getVocabulary().size());
        Map<String, Long> termAppearingDocCount = getTermAppearingDocCount();
        IntStream.range(0, getTokenizedDataSetSize())
                .forEach(rowIndex -> corpus.assignRow(rowIndex, vectorizeDoc(rowIndex, termAppearingDocCount)));
        dataset.setMatrix(corpus);
    }

    private SequentialAccessSparseVector vectorizeDoc(int doc, Map<String, Long> termAppearingDocCount) {
        SequentialAccessSparseVector vector = new SequentialAccessSparseVector(dataset.getVocabulary().size());
        Map<String, Long> termFrequencyInDoc = calculateTermFrequencyInDoc(doc);
        double docWordCount = termFrequencyInDoc.values().stream().mapToDouble(Long::doubleValue).sum();
        termFrequencyInDoc.forEach((word, frequency) -> {
            final int wordIndex = Collections.binarySearch(dataset.getVocabulary(), word);
            final Double tfIdf = calculateTFIDF(frequency.doubleValue(), docWordCount, getTokenizedDataSetSize(), termAppearingDocCount.get(word).doubleValue());
            vector.set(wordIndex, tfIdf);
        });
        return vector;
    }

    private Map<String, Long> calculateTermFrequencyInDoc(int doc) {
        return Arrays.stream(tokenizedDataset.get(doc)).collect(groupingBy(Function.identity(), counting()));
    }

    private int getTokenizedDataSetSize() {
        return tokenizedDataset.size();
    }

    private Double calculateTFIDF(double docTermAppearanceCount, double docWordCount, double docCount, double termAppearingDocCount) {
        double termFrequency = docTermAppearanceCount / docWordCount;
        double inverseDocumentFrequency = Math.log(docCount / termAppearingDocCount);
        return termFrequency * inverseDocumentFrequency;
    }

    private String[] tokenizeThesisAbstract(Thesis thesis) {
        return thesis.getTr().toLowerCase(new Locale("tr", "TR")).replaceAll("\\p{Punct}+", "").split("\\s+");
    }

    private void calculate(OperationType type, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long completionTime = System.currentTimeMillis() - start;
        System.out.println(type.getLogPrefix() + " took: " + completionTime / 1000 + " seconds.");
    }
}