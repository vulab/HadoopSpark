package com.vulab.code;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SimpleVulabKafkaConsumer {
	
	public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "sandbox.hortonworks.com:6667");	
        props.put("group.id", "mygroup");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("mytopic"));

        boolean running = true;
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
            	System.out.println(record.offset());
                System.out.println(record.value());
            }
        }
        consumer.close();
    }
}
