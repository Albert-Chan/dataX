package com.dataminer.websocket.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.dataminer.websocket.client.ReceiverSocket;

public class ReceiverClientTest {
	public static void main(String[] args) throws Exception {
		String destUri = "ws://127.0.0.1:7777/test/";

		WebSocketClient client = new WebSocketClient();
		ReceiverSocket socket = new ReceiverSocket();
		try {
			client.start();
			URI echoUri = new URI(destUri);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socket, echoUri, request);
			System.out.printf("Connecting to : %s%n", echoUri);
			TimeUnit.SECONDS.sleep(30);
			//socket.awaitClose(30, TimeUnit.SECONDS);
		} finally {
//			client.stop();
		}
	}
}
