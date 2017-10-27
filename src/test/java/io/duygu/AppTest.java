package io.duygu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.duygu.dto.Thesis;
import io.duygu.preprocess.DatasetParser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    public static final String TEST_FILE_PATH = "";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws FileNotFoundException {
        DatasetParser preprocessor = new DatasetParser();
        long start = System.currentTimeMillis();
        List<Thesis> theses = preprocessor.preprocess(TEST_FILE_PATH);
        long completionTime = System.currentTimeMillis() - start;
        System.out.printf("Created parse took: " + completionTime / 1000 + " seconds.");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Thesis>>() {
        }.getType();
        start = System.currentTimeMillis();
        gson.fromJson(new FileReader(new File(TEST_FILE_PATH)), listType);
        completionTime = System.currentTimeMillis() - start;
        System.out.printf("Gson parse took: " + completionTime / 1000 + " seconds.");
    }
}
