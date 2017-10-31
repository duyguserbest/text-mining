package io.duygu.preprocess;

public class TFIDFCalculator {

    public static Double calculate(double docTermAppearanceCount, double docWordCount, double docCount,
                                        double termAppearingDocCount) {
        double termFrequency = docTermAppearanceCount / docWordCount;
        double inverseDocumentFrequency = Math.log(docCount / termAppearingDocCount);
        return termFrequency * inverseDocumentFrequency;
    }
}
