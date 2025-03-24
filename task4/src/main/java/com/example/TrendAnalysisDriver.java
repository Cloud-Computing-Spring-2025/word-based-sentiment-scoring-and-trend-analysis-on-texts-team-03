package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TrendAnalysisDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Trend Analysis by Decade");

        job.setJarByClass(TrendAnalysisDriver.class);
        job.setMapperClass(TrendAnalysisMapper.class);
        job.setReducerClass(TrendAnalysisReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));  // /input/task4_dataset
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // /output/task4_output

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
