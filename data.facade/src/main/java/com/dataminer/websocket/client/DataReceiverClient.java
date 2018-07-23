package com.dataminer.websocket.client;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class DataReceiverClient {
	private WebSocketClient client;
	private DataReceiverSocket socket = new DataReceiverSocket();

	private Future<Session> futureSession;

	public DataReceiverClient() {
		this.client = new WebSocketClient();
	}

	public Future<Session> connect(String destURI) throws Exception {
		this.client.start();
		URI echoUri = new URI(destURI);
		ClientUpgradeRequest request = new ClientUpgradeRequest();
		this.futureSession = client.connect(socket, echoUri, request);
		return this.futureSession;
	}

	public void send(String msg) throws IOException, InterruptedException, ExecutionException {
		Session session = futureSession.get();
		RemoteEndpoint remote = session.getRemote();
		remote.sendString(msg);
	}

	public void stop() throws Exception {
		if (Objects.nonNull(client)) {
			client.stop();
		}
	}
	
	public static void main(String[] args) {
		String destUri = "ws://127.0.0.1:7777/test/";
		if (args.length > 0) {
			destUri = args[0];
		}
		WebSocketClient client = new WebSocketClient();
		DataReceiverSocket socket = new DataReceiverSocket();
		try {
			client.start();
			URI echoUri = new URI(destUri);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socket, echoUri, request);
			System.out.printf("Connecting to : %s%n", echoUri);
			socket.awaitClose(35, TimeUnit.SECONDS);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}