package io.duygu;

import io.duygu.dto.Dataset;
import io.duygu.dto.Thesis;
import io.duygu.preprocess.DatasetBuilder;
import io.duygu.preprocess.DatasetPreprocessor;
import io.duygu.preprocess.DatasetReader;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.exit(1);
        }

//        JCommander jCommander = new JCommander();
//        CommanDLineArguments

        DatasetReader reader = new DatasetReader();
        List<Thesis> thesisList = reader.getDocs(args[0]);
        DatasetPreprocessor preprocessor = new DatasetPreprocessor();
        List<Thesis> theses = preprocessor.process(thesisList);
        Dataset data = new DatasetBuilder().createDataset(theses);

    }

}