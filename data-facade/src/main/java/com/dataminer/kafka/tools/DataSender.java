package com.dataminer.kafka.tools;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import com.dataminer.configuration.ConfigManager;
import com.dataminer.monitor.Constants;

public class DataSender {
	protected static final Logger LOG = Logger.getLogger(DataSender.class);

	private Producer<String, String> producer;
	private String topic;

	public DataSender() {
		ConfigManager conf = ConfigManager.getConfig();
		String brokers = conf.getProperty(Constants.KAFKA_BROKERS);
		this.topic = conf.getProperty(Constants.KAFKA_MONITOR_TOPIC);

		if (!isEmptyString(brokers) && !isEmptyString(this.topic)) {
			Properties props = new Properties();
			props.put("bootstrap.servers", brokers);
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 1024);
			props.put("linger.ms", 15000);
			props.put("buffer.memory", 10240);
			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

			this.producer = new KafkaProducer<String, String>(props);
		}
	}

	private boolean isEmptyString(String testString) {
		return (null == testString) || testString.isEmpty();
	}

	public void close() {
		if (null != producer) {
			producer.close();
		}
	}

	public void flush() {
		if (null != producer) {
			producer.flush();
		}
	}

	public void send(String key, String message) {
		if (null == producer) {
			return;
		}
		producer.send(new ProducerRecord<String, String>(topic, key, message), new Callback() {
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					LOG.error(e);
				}
			}
		});
	}

}
