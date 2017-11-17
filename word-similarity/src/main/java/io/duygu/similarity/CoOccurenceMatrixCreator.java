package io.duygu.similarity;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseRowMatrix;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by dserbest on 17.11.2017.
 */
public class CoOccurenceMatrixCreator {

    public static final String WHITESPACE_PATTERN = "\\s+";

    private Matrix wordVectors;

    private List<String> vocabulary;

    private Matrix matrix;

    public CoOccurenceMatrixCreator(Matrix wordVectors, List<String> vocabulary) {
        this.wordVectors = wordVectors;
        this.vocabulary = vocabulary;
        this.matrix = new SparseRowMatrix(wordVectors.rowSize(), wordVectors.columnSize());
    }

    public Matrix calculateCoOccurence(List<String> documents) {
        documents.stream().forEach(document -> {
            final String[] words = document.split(WHITESPACE_PATTERN);
            IntStream.range(0, words.length).forEach(wordIndex -> {
                final int rowId = Collections.binarySearch(vocabulary, words[wordIndex]);
                if ((rowId - 1) >= 0) {
                    sumVectors(rowId, words[(rowId - 1)]);
                }
                if ((rowId + 1) < words.length) {
                    sumVectors(rowId, words[(rowId + 1)]);
                }
            });
        });
        return matrix;
    }

    private void sumVectors(int cooccurenceMatrixRowIndex, String cooccuredWord) {
        matrix.assignRow(cooccurenceMatrixRowIndex, matrix.viewRow(cooccurenceMatrixRowIndex).plus(
            wordVectors.viewRow(Collections.binarySearch(vocabulary, cooccuredWord))));
    }

}
