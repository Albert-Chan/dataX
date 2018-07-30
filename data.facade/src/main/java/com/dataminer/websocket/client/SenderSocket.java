package com.dataminer.websocket.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class SenderSocket {
	protected static final Logger LOG = Logger.getLogger(SenderSocket.class);

	private final CountDownLatch closeLatch;

	public SenderSocket() {
		this.closeLatch = new CountDownLatch(1);
	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		this.closeLatch.countDown();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws InterruptedException, ExecutionException {
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String msg) {
		// The sender ignores all incoming message.
	}
}