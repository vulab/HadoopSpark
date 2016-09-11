package com.vulab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

public class SparkStreamingProcessFromKafka {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaWordCount");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));
		int numThreads = Integer.parseInt("1");
		Map<String, Integer> topicMap = new HashMap<>();
		String[] topics = "topic1,topic2".split(",");
		for (String topic : topics) {
			topicMap.put(topic, numThreads);
		}

		JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, "sandbox.hortonworks.com",
				"consumergroup", topicMap);

		JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
			@Override
			public String call(Tuple2<String, String> tuple2) {
				System.out.println("VALUE IS " + tuple2._2());
				return tuple2._2();
			}
		});

	}

}
