package com.example;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.*;
import java.util.*;

public class SentimentMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Map<String, Integer> lexicon = new HashMap<>();
    private final IntWritable sentimentScore = new IntWritable();
    private final Text bookYearKey = new Text();

    @Override
    protected void setup(Context context) throws IOException {
        // ✅ Hardcoded absolute path where AFINN-111.txt is placed inside the container
        BufferedReader reader = new BufferedReader(new FileReader("/opt/hadoop/AFINN-111.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length == 2) {
                lexicon.put(parts[0].toLowerCase(), Integer.parseInt(parts[1]));
            }
        }
        reader.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Sample line: bookID|year<TAB>description text...
        String[] metaText = value.toString().split("\t", 2);
        if (metaText.length < 2) return;

        String[] meta = metaText[0].split("\\|");
        if (meta.length < 2) return;

        String bookId = meta[0];
        String year = meta[1];
        String description = metaText[1].toLowerCase();

        String[] tokens = description.replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        for (String token : tokens) {
            if (lexicon.containsKey(token)) {
                bookYearKey.set(bookId + "|" + year);
                sentimentScore.set(lexicon.get(token));
                context.write(bookYearKey, sentimentScore);
            }
        }
    }
}
