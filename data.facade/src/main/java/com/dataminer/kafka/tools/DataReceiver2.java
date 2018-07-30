package com.dataminer.kafka.tools;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.dataminer.configuration.ConfigManager;
import com.dataminer.monitor.Constants;

public class DataReceiver2 {

	private static Consumer<String, String> createConsumer() {

		ConfigManager conf = ConfigManager.getConfig();
		String brokers = conf.getProperty(Constants.KAFKA_BROKERS);
		String topic = conf.getProperty(Constants.KAFKA_MONITOR_TOPIC);

		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_receiver");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		// Create the consumer using props.
		final Consumer<String, String> consumer = new KafkaConsumer<>(props);

		// Subscribe to the topic.
		consumer.subscribe(Collections.singletonList(topic));
		return consumer;
	}

	public static void runConsumer() throws InterruptedException {
		final Consumer<String, String> consumer = createConsumer();

		final int giveUp = 100;
		int noRecordsCount = 0;

		while (true) {
			final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000*60);

			if (consumerRecords.count() == 0) {
				noRecordsCount++;
				if (noRecordsCount > giveUp)
					break;
				else
					continue;
			}

			consumerRecords.forEach(record -> {
				System.out.printf("%s%n", record.value());
			});

			consumer.commitAsync();
		}
		consumer.close();
		System.out.println("DONE");
	}

}
