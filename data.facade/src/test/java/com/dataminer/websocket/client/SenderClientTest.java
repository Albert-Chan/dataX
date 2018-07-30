package com.dataminer.websocket.client;

public class SenderClientTest {
	public static void main(String[] args) throws Exception {
		String destURI = "ws://127.0.0.1:7777/test/";
		SenderClient sender = new SenderClient();
		try {
			sender.connect(destURI);
			sender.send("ddd");
		} finally {
			sender.stop();
		}
	}
}
