package io.duygu.dto;

import org.apache.mahout.math.Matrix;

import java.util.List;

public class Dataset {

    private List<Thesis> dataset;

    private List<String> vocabulary;

    private Matrix matrix;

    public Dataset() {
    }

    public Dataset(List<Thesis> dataset) {
        this.dataset = dataset;
    }

    public List<Thesis> getDataset() {
        return dataset;
    }

    public void setDataset(List<Thesis> dataset) {
        this.dataset = dataset;
    }

    public List<String> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<String> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
