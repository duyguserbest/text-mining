package io.duygu.clustering.algorithm;

import io.duygu.dto.Dataset;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMeansClustererTest extends TestCase {

    public void testFindMostSimiliarCentroid() {
        List<List<Double>> datasetCentroidSimilarityValues = generateDatasetCentroidSimilarityValues();
        KMeansClusterer kMeansClusterer = new KMeansClusterer(new Dataset());
        Assert.assertEquals(kMeansClusterer.findMostSimiliarCentroid(datasetCentroidSimilarityValues, 0),1);
    }

    public List<List<Double>> generateDatasetCentroidSimilarityValues() {
        List<List<Double>> datasetCentroidSimilarityValues = new ArrayList<>();
        datasetCentroidSimilarityValues.add(Arrays.asList(10d));
        datasetCentroidSimilarityValues.add(Arrays.asList(25d));
        datasetCentroidSimilarityValues.add(Arrays.asList(15d));
        datasetCentroidSimilarityValues.add(Arrays.asList(20d));
        return datasetCentroidSimilarityValues;
    }


}