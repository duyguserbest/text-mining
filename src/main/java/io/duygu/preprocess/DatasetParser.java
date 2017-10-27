package io.duygu.preprocess;

import io.duygu.dto.Thesis;
import org.apache.mahout.math.SequentialAccessSparseVector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatasetParser {

    public static final String TURKISH_ABSTRACT_PATTERN = "\"tr\": \".*\"";

    public static final String ABSTRACT_TAG = "\"tr\": ";

    public static final String META_TAG_PATTERN = "\"meta\": \".*\"";

    private List<String> vocabulary;

    private Pattern abstractPattern;

    private Pattern metaPattern;

    public DatasetParser() {
        this.vocabulary = new ArrayList<>();
        this.abstractPattern = Pattern.compile(TURKISH_ABSTRACT_PATTERN);
        this.metaPattern = Pattern.compile(META_TAG_PATTERN);
    }

    public List<Thesis> preprocess(String filePath) {
        List<Thesis> theses = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner sc = new Scanner(inputStream, "UTF-8")) {
            boolean isMetaFound = false;
            boolean isAbstractFound = false;
            Thesis thesis = new Thesis();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher abstractMatcher = abstractPattern.matcher(line);
                Matcher metaMatcher = metaPattern.matcher(line);
                if (abstractMatcher.find()) {
                    String thesisAbstract = abstractMatcher.group().replaceFirst(ABSTRACT_TAG, "").replaceAll("\\p{Punct}+", "");
                    thesis.setTr(thesisAbstract);
                    isAbstractFound = true;
//                    vectorizeDoc(abstractMatcher.group());
                } else if (metaMatcher.find()) {
                    String categoryDirty = metaMatcher.group().split("\\\\n")[3];
                    String[] split = categoryDirty.split("/");
                    String category = split[split.length - 1];
                    thesis.setMeta(category);
                    isMetaFound = true;
                }

                if(isAbstractFound && isMetaFound){
                    theses.add(thesis);
                    thesis = new Thesis();
                    isAbstractFound = false;
                    isMetaFound = false;
                }
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
//        SparseRowMatrix matrix = new SparseRowMatrix();
        return theses;
    }

    private void vectorizeDoc(String abstractText) {
        String thesisAbstract = abstractText.replaceFirst(ABSTRACT_TAG, "").replaceAll("\\p{Punct}+", "");
        String[] tokens = thesisAbstract.split("\\s+");
        SequentialAccessSparseVector vector = new SequentialAccessSparseVector();
        Arrays.stream(tokens).forEach(token -> {
            double frequencyOfToken = 1;
            int indexOfToken = vocabulary.indexOf(token);
            if (indexOfToken == -1) {
                vocabulary.add(token);
            } else {
                frequencyOfToken = frequencyOfToken + 1d;
            }
            vector.set(indexOfToken, frequencyOfToken);
        });
    }
}
