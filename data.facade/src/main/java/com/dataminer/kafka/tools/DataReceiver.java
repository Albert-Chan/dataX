package com.dataminer.kafka.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dataminer.consumer.IDataConsumer;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;

public class DataReceiver {
	private static final Logger LOG = LoggerFactory.getLogger(DataReceiver.class);

	private final ConsumerConnector connector;
	private final String topic;
	private List<IDataConsumer> consumers = new ArrayList<IDataConsumer>();

	public DataReceiver(String topic, String group, String zkConnect) {
		this.topic = topic;

		Properties props = new Properties();
		props.put("zookeeper.connect", zkConnect);
		props.put("group.id", group);
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

		connector = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
	}

	public void registerConsumer(IDataConsumer consumer) {
		consumers.add(consumer);
	}

	public void handle() throws Exception {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, Integer.valueOf(1));
		Map<String, List<KafkaStream<String, String>>> consumerMap = connector.createMessageStreams(topicCountMap,
				new StringDecoder(null), new StringDecoder(null));
		KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext()) {
			String message = it.next().message();
			for (IDataConsumer consumer : consumers) {
				consumer.consume(message);
			}
		}
	}

}