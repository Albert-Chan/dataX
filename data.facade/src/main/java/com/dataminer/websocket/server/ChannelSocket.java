package com.dataminer.websocket.server;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 128 * 1024, maxBinaryMessageSize = 128 * 1024)
public class ChannelSocket {
	protected static final Logger LOG = Logger.getLogger(ChannelSocket.class);

	private static final Queue<Session> endpoints = new ConcurrentLinkedQueue<Session>();

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		LOG.info("server: @OnWebSocketConnect");
		if (session.isOpen()) {
			endpoints.offer(session);
		}
	}

	@OnWebSocketClose
	public void OnWebSocketClose(Session session, int i, String string) {
		endpoints.remove(session);
		LOG.info("server: @OnWebSocketClose");
	}

	@OnWebSocketMessage
	public void onMessage(Session thisSession, String msg) throws IOException {
		LOG.info(String.format("<<<<<<<<<<<<<<<<<<%s%n", msg));
		for (Session session : endpoints) {
			if ((!session.equals(thisSession)) && session.isOpen()) {
				session.getRemote().sendString(msg);
			}
		}
	}

	// @OnWebSocketBinary
	// public void onWebSocketBinary(Session session, int a, String s) {
	//
	// }

}