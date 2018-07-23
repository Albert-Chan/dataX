package com.dataminer.kafka.tools;

public class DataSenderTest {
	public static void main(String[] args) throws InterruptedException {
		DataSender sender = new DataSender();
		sender.send("", "this is a test message. :)");

		sender.close();
	}

}