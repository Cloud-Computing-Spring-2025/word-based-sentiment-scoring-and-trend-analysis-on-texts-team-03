package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PreprocessingMapper extends Mapper<LongWritable, Text, Text, Text> {

    // Stopwords list
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
            "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
            "their", "then", "there", "these", "they", "this", "to", "was", "will", "with"));

    // Regex to detect URLs (including Google Books and others)
    private static final Pattern URL_PATTERN = Pattern.compile(
            "http[s]?://[a-zA-Z0-9./_?&=%-]+", Pattern.CASE_INSENSITIVE);

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header row
        if (line.startsWith("isbn13")) {
            return;
        }

        // Expecting CSV format: isbn13, title, authors, published_year, description
        String[] parts = line.split(",", 5);

        // Ensure the row has at least 5 columns
        if (parts.length < 5) {
            return; // Skip malformed lines
        }

        // Extract only the necessary fields
        String isbn13 = parts[0].trim(); // Keep
        String title = parts[1].trim(); // Keep
        String authors = parts[2].trim(); // Keep
        String published_year = parts[3].trim(); // Keep
        String description = parts[4].trim(); // Keep

        // Ensure published_year is valid (should be a 4-digit number)
        if (!Pattern.matches("\\d{4}", published_year)) {
            published_year = "unknown"; // Assign "unknown" if missing
        }

        // Clean the description text (remove URLs, stopwords, punctuation)
        String cleanedText = cleanText(description);

        // Ensure that cleaned text is not empty
        if (cleanedText.isEmpty()) {
            cleanedText = "No description available";
        }

        // Format output correctly
        String compositeKey = isbn13 + "|" + published_year;
        String outputValue = title + " :: " + cleanedText;

        // Emit output
        context.write(new Text(compositeKey), new Text(outputValue));
    }

    private String cleanText(String text) {
        text = text.toLowerCase(); // Convert to lowercase
        text = URL_PATTERN.matcher(text).replaceAll(""); // Remove URLs
        text = text.replaceAll("[^a-z\\s]", ""); // Remove special characters
        StringTokenizer tokenizer = new StringTokenizer(text);
        StringBuilder cleaned = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (!STOPWORDS.contains(word)) {
                cleaned.append(word).append(" ");
            }
        }
        return cleaned.toString().trim();
    }
}
