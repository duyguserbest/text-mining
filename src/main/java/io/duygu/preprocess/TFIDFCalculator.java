package io.duygu.preprocess;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class TFIDFCalculator {

    private Map<String, Long> termAppearingDocCountMap;

    private List<String[]> tokenizedDataset;

    public TFIDFCalculator(List<String[]> tokenizedDataset) {
        this.tokenizedDataset = tokenizedDataset;
        termAppearingDocCountMap = getTermAppearingDocCount();
    }

    private Map<String, Long> getTermAppearingDocCount() {
        return tokenizedDataset.stream().map(vector -> Arrays.stream(vector).collect(Collectors.toSet())).flatMap(Set::stream)
                .collect(groupingBy(Function.identity(), counting()));
    }


    private Double calculateTFIDF(double docTermAppearanceCount, double docWordCount, String word) {
        double termAppearingDocCount = termAppearingDocCountMap.get(word).doubleValue();
        double docCount = tokenizedDataset.size();
        double termFrequency = docTermAppearanceCount / docWordCount;
        double inverseDocumentFrequency = Math.log(docCount / termAppearingDocCount);
        return termFrequency * inverseDocumentFrequency;
    }
}
