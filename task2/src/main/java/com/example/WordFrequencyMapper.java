package com.example;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import edu.stanford.nlp.simple.Sentence;

public class WordFrequencyMapper extends Mapper<Object, Text, Text, IntWritable> {
    private static final Pattern NON_ALPHABETIC = Pattern.compile("[^a-zA-Z]");
    private final IntWritable one = new IntWritable(1);

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();

        // Skip empty lines or header
        if (line.isEmpty() || line.startsWith("isbn13")) return;

        // Expected format: isbn13|year    title :: cleaned_description
        String[] parts = line.split("\\t", -1);
        if (parts.length < 2) return;

        String[] metaData = parts[0].split("\\|", -1);
        if (metaData.length < 2) return;

        String bookID = metaData[0].trim();
        String year = metaData[1].trim();

        // Ensure bookID and year are present
        if (bookID.isEmpty() || year.isEmpty()) return;

        String[] titleDesc = parts[1].split("::", -1);
        if (titleDesc.length < 2) return;

        String description = titleDesc[1].trim();
        if (description.isEmpty()) return;

        // Tokenize and lemmatize words
        for (String word : description.split("\\s+")) {
            word = NON_ALPHABETIC.matcher(word).replaceAll("").toLowerCase();

            if (!word.isEmpty()) {
                try {
                    // Lemmatize the word using Stanford NLP
                    String lemma = lemmatize(word);
                    String outputKey = bookID + "|" + lemma + "|" + year;
                    context.write(new Text(outputKey), one);
                } catch (Exception e) {
                    System.err.println("Error in lemmatization: " + e.getMessage());
                }
            }
        }
    }

    // Lemmatization using Stanford NLP
    private String lemmatize(String word) {
        try {
            Sentence sentence = new Sentence(word);
            List<String> lemmas = sentence.lemmas();
            return lemmas.isEmpty() ? word : lemmas.get(0);
        } catch (Exception e) {
            return word; // Return the original word if lemmatization fails
        }
    }
}
