package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * Mapper for Trend Analysis by Decade
 * Expected input format per line: bookID | year   sentimentScore
 * Example: 9780006178736 | 1993    3
 */
public class TrendAnalysisMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final Text outputKey = new Text();
    private final IntWritable outputValue = new IntWritable();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Log the input line (optional debug)
        System.out.println("Processing Line: " + value.toString());

        // Check and skip empty lines
        if (value.toString().trim().isEmpty()) return;

        // Split the line by '|' to separate bookID and the rest
        String[] lineParts = value.toString().split("\\|");
        if (lineParts.length < 2) {
            System.out.println("Skipping badly formatted line: " + value.toString());
            return;
        }

        String bookId = lineParts[0].trim();
        String[] yearAndScore = lineParts[1].trim().split("\\s+");

        if (yearAndScore.length < 2) {
            System.out.println("Skipping incomplete data: " + value.toString());
            return;
        }

        try {
            int year = Integer.parseInt(yearAndScore[0].trim());
            int sentimentScore = Integer.parseInt(yearAndScore[1].trim());
            int decade = (year / 10) * 10;

            // Emit the (bookID | decade) as key, sentimentScore as value
            outputKey.set(bookId + " | " + decade);
            outputValue.set(sentimentScore);
            context.write(outputKey, outputValue);
        } catch (NumberFormatException e) {
            System.out.println("Skipping line due to NumberFormatException: " + value.toString());
        }
    }
}
