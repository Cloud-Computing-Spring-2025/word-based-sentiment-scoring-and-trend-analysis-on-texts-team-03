package com.example;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PreprocessingReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String title = "";
        StringBuilder combinedText = new StringBuilder();

        for (Text val : values) {
            String[] parts = val.toString().split(" :: ", 2); // Ensure correct delimiter
            if (parts.length == 2) {
                title = parts[0];

                // Avoid duplicate "No description available"
                if (!combinedText.toString().contains(parts[1])) {
                    combinedText.append(parts[1]).append(" ");
                }
            }
        }

        String finalText = combinedText.toString().trim();

        // If no text found, add "No description available" only once
        if (finalText.isEmpty() || finalText.equals("No description available")) {
            finalText = "No description available";
        }

        String outputValue = title + " :: " + finalText;
        context.write(key, new Text(outputValue));
    }
}
