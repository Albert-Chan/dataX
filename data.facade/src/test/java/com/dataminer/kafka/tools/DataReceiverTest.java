package com.dataminer.kafka.tools;

import com.dataminer.consumer.impl.DBInsertionConsumer;
import com.dataminer.data.pojo.TrafficStatus;

public class DataReceiverTest {
	public static void main(String[] args) throws Exception {
		DataReceiver receiver = new DataReceiver("test", "", "zkConnect");

		DBInsertionConsumer<TrafficStatus> consumer = new DBInsertionConsumer<>(
				"insert into TBL_TRAFF values (?, ?, ?, ?, ?)", (ps, ele) -> {
					ps.setLong(1, ele.getRoadId());
					ps.setTimestamp(2, ele.getTime());
					ps.setFloat(3, ele.getAvgSpeed());
					ps.setFloat(4, ele.getAvgDuration());
					ps.setInt(5, ele.getStatus());
					return ps;
				});

		receiver.registerConsumer(consumer);
		receiver.handle();
	}
}