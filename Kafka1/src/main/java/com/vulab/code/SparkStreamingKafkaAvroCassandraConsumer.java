package com.vulab.code;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;

import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;

public class SparkStreamingKafkaAvroCassandraConsumer {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("kafka-sandbox")
                .setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(2000));

        Set<String> topics = Collections.singleton("mytopic");
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "sandbox.hortonworks.com:6667");

        JavaPairInputDStream<String, byte[]> directKafkaStream = KafkaUtils.createDirectStream(ssc,
                String.class, byte[].class, StringDecoder.class, DefaultDecoder.class, kafkaParams, topics);

        directKafkaStream.foreachRDD(rdd -> {
            rdd.foreach(avroRecord -> {
                Schema.Parser parser = new Schema.Parser();
                Schema schema = parser.parse(AvroVulabProducer.USER_SCHEMA);
                Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
                GenericRecord record = recordInjection.invert(avroRecord._2).get();
                saveToCassandra(record);
                System.out.println("str1= " + record.get("str1")
                        + ", str2= " + record.get("str2")
                        + ", int1=" + record.get("int1"));
            });
        });

        ssc.start();
        ssc.awaitTermination();
    }
    
    public static void saveToCassandra(GenericRecord record){
    	Cluster cluster;
		Session session;
		ResultSet results;
		Row rows;

		cluster = Cluster.builder().addContactPoint("localhost").withPort(9042)
				.withRetryPolicy(DefaultRetryPolicy.INSTANCE).build();
		
		session = cluster.connect("vulab");
		
		PreparedStatement statement = session
				.prepare("INSERT INTO client_records" + "(str1, str2, int1) VALUES (?,?,?);");

		BoundStatement boundStatement = new BoundStatement(statement);
		
		System.out.println("str1= " + record.get("str1")
        + ", str2= " + record.get("str2")
        + ", int1=" + record.get("int1"));
		
		Utf8 str1 = (Utf8) record.get("str1");
		Utf8 str2 = (Utf8) record.get("str2");
		Integer int1 = (Integer) record.get("int1");
		
		session.execute(boundStatement.bind(str1.toString(), str2.toString(),int1));
		
		cluster.close();
    }


}
