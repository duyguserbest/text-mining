package io.duygu.similarity.preprocess;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by dserbest on 17.11.2017.
 */
public class DatasetPreprocess {

    public static final int VOCABULARY_SIZE = 50000;

    public static final String UTF_8_CHARSET = "UTF8";

    public static final String LINE_SPLIT_SPACE = " ";

    public static List<String> findMostPopularWords(List<String> documents) throws IOException {
        return countWords(documents).entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                    .limit(VOCABULARY_SIZE).map(stringLongEntry -> stringLongEntry.getKey()).sorted()
                                    .collect(toList());
    }

    private static Map<String, Long> countWords(List<String> documents) throws IOException {
        return documents.stream().flatMap(line -> Arrays.stream(line.split(LINE_SPLIT_SPACE)))
                        .collect((groupingBy(Function.identity(), counting())));
    }

    public static List<String> readFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath), Charset.forName(UTF_8_CHARSET)).collect(toList());
    }

}
