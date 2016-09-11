package com.vulab;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class WordCountSpark {
	public static void main(String[] args) throws Exception {
		String inputFile = "input.txt";
		String outputFile = "output.txt";
		// Create a Java Spark Context.
		// System.setProperty("hadoop.home.dir", "C:\\winutils\\");
		SparkConf conf = new SparkConf().setAppName("wordCount").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Load our input data.
		JavaRDD<String> input = sc.textFile(inputFile);
		// Split up into words.
		JavaRDD<String> words = input.flatMap(new FlatMapFunction<String, String>() {
			public Iterable<String> call(String x) {
				return Arrays.asList(x.split(" "));
			}
		});
		// Transform into word and count.
		JavaPairRDD<String, Integer> counts = words.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String x) {
				return new Tuple2(x, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer x, Integer y) {
				return x + y;
			}
		});
		// Save the word count back out to a text file, causing evaluation.
		counts.saveAsTextFile(outputFile);
	}
}