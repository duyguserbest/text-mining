package io.duygu.similarity.vector;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.SparseRowMatrix;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by dserbest on 17.11.2017.
 */
public class Word2RandomVector {

    private int vectorDimension;

    private int nonZeroElementCount;

    private Random random;

    public Word2RandomVector(int vectorDimension, int nonZeroElementCount) {
        this.vectorDimension = vectorDimension;
        this.nonZeroElementCount = nonZeroElementCount;
        this.random = new Random();
    }

    public Matrix generateMatrix(int vocabularySize) {
        Matrix matrix = new SparseRowMatrix(vocabularySize, vectorDimension);
        IntStream.range(0, vocabularySize).parallel().forEach(index -> matrix.assignRow(index, generateRandomVector()));
        return matrix;
    }

    public SequentialAccessSparseVector generateRandomVector() {
        SequentialAccessSparseVector vector = new SequentialAccessSparseVector(vectorDimension);
        IntStream.range(0, nonZeroElementCount).forEach(index -> generateVectorElement(index, vector));
        return vector;
    }

    private void generateVectorElement(int index, SequentialAccessSparseVector vector) {
        int vectorIndex;
        do {
            vectorIndex = generateRandomIndex();
        }
        while (vector.get(vectorIndex) == 0d);
        vector.set(vectorIndex, index % 2 == 0 ? 1d : -1d);
    }

    private int generateRandomIndex() {
        return random.nextInt(vectorDimension);
    }

}
