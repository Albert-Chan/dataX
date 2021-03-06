package com.dataminer.websocket.client;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class SenderClient {
	private WebSocketClient client;
	private SenderSocket socket = new SenderSocket();

	private Future<Session> futureSession;

	public SenderClient() {
		this.client = new WebSocketClient();
	}

	public Future<Session> connect(String destURI) throws Exception {
		client.start();
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

}