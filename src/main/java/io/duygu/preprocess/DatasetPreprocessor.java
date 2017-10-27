package io.duygu.preprocess;

import io.duygu.dto.Thesis;

import java.util.List;
import java.util.stream.Collectors;

public class DatasetPreprocessor {

    public List<Thesis> process(List<Thesis> thesisList) {
        long start = System.currentTimeMillis();
        List<Thesis> dataset = eliminateNullAbstracts(thesisList);
        long completionTime = System.currentTimeMillis() - start;
        System.out.println("Filtering out thesis with null abstracts took: " + completionTime / 1000 + " seconds.");
        return dataset;
    }

    private List<Thesis> eliminateNullAbstracts(List<Thesis> thesisList) {
        return thesisList.parallelStream().filter(thesis -> thesis.getTr() != null).collect(Collectors.toList());
    }

}
