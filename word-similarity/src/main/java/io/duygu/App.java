package io.duygu;

import io.duygu.similarity.CoOccurenceMatrixCreator;
import io.duygu.similarity.preprocess.DatasetPreprocess;
import io.duygu.similarity.vector.Word2RandomVector;
import org.apache.mahout.math.Matrix;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.exit(1);
        }

        String filePath = args[0];
        final Integer vectorDimension = Integer.valueOf(args[1]);
        final Integer nonZeroElementCount = Integer.valueOf(args[2]);

        if (vectorDimension <= nonZeroElementCount) {
            System.out.println("Random vector dimensions should be larger than non-zero element count.");
            System.exit(1);
        }

        final long start = System.currentTimeMillis();
        final List<String> documents = DatasetPreprocess.readFile(filePath);
        final List<String> vocabulary = DatasetPreprocess.findMostPopularWords(documents);

        Word2RandomVector vector = new Word2RandomVector(vectorDimension, nonZeroElementCount);
        Matrix matrix = vector.generateMatrix(vocabulary.size());

        CoOccurenceMatrixCreator matrixCreator = new CoOccurenceMatrixCreator(matrix, vocabulary);
        matrixCreator.calculateCoOccurence(documents);

        System.out.println((System.currentTimeMillis() - start) / 1000);

    }

}
