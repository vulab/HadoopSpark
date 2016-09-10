package com.vulab.code;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleVulabKafkaProducer {
	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
		
		//update etc/hosts file to have the ip configured properly
		props.put("bootstrap.servers", "sandbox.hortonworks.com:6667");		
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");		
				

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 100; i++) {
			ProducerRecord<String, String> record = new ProducerRecord<>("mytopic", "value-" + i);
			System.out.println("sending "+i);
			producer.send(record);
			//Thread.sleep(250);
		}		

		producer.close();
	}
}
