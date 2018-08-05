package com.dataminer.kafka.tools;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.fastjson.JSON;
import com.dataminer.data.pojo.TrafficStatus;

public class DataSenderTest {
	public static void main(String[] args) throws InterruptedException {

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
//		DataSender[] senders = new DataSender[10];
//		for (int i = 0; i < 10; i++) {
//			senders[i] = new DataSender();
//		}
		for (int i = 0; i < 10; i++) {
			final int roadId = i;
			final DataSender sender = new DataSender();//senders[i];
			executor.execute(() -> {
				sender.send("", "Data transferring started...");
				Timer timer = new Timer();
				TimerTask task = new DataSenderTask(sender, roadId);
				timer.schedule(task, 0, 10_000);
			});
		}
		System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());

		executor.shutdown();
		System.out.println("executor was shut down.");
//		for (int i = 0; i < 10; i++) {
//			senders[i].close();
//		}
	}

}

class DataSenderTask extends TimerTask {
	private DataSender sender;
	private int roadId;

	public DataSenderTask(DataSender sender, int roadId) {
		this.sender = sender;
		this.roadId = roadId;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			TrafficStatus ts = new TrafficStatus();
			ts.setRoadId(roadId);
			ts.setTime(new Timestamp(System.currentTimeMillis()));
			// ts.setAvgSpeed(avgSpeed);
			// ts.setAvgDuration(avgDuration);
			ts.setStatus(i);
			sender.send("", JSON.toJSONString(ts));
		}
		sender.flush();
	}
}