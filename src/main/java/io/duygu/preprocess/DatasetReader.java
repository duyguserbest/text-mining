package io.duygu.preprocess;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.duygu.dto.Thesis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class DatasetReader {
    public List<Thesis> getDocs(String filePath) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<Thesis> thesisList = getThesisList(filePath);
        long completionTime = System.currentTimeMillis() - start;
        System.out.println("Json parse took: " + completionTime / 1000 + " seconds.");
        return thesisList;
    }

    private List<Thesis> getThesisList(String filePath) throws FileNotFoundException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Thesis>>() {
        }.getType();
        return (List<Thesis>) gson.fromJson(new FileReader(new File(filePath)), listType);
    }
}
