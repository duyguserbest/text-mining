package io.duygu.preprocess;

import io.duygu.dto.Thesis;

import java.util.List;
import java.util.stream.Collectors;

public class DatasetPreprocessor {

    public static List<Thesis> eliminateNullAbstracts(List<Thesis> thesisList) {
        return thesisList.parallelStream().filter(thesis -> thesis.getTr() != null).collect(Collectors.toList());
    }

}
