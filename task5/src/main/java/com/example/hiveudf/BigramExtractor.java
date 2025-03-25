package com.example.hiveudf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import java.util.ArrayList;
import java.util.List;

public class BigramExtractor extends UDF {
    public List<String> evaluate(String input) {
        List<String> bigrams = new ArrayList<>();
        if(input == null || input.trim().isEmpty()) return bigrams;

        String[] tokens = input.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");
        for(int i = 0; i < tokens.length - 1; i++) {
            bigrams.add(tokens[i] + " " + tokens[i + 1]);
        }
        return bigrams;
    }
}