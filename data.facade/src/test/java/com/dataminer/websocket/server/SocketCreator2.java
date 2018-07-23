package com.dataminer.websocket.server;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class SocketCreator2 implements WebSocketCreator {
//	private MyBinaryEchoSocket binaryEcho;
//	private MyEchoSocket textEcho;

//	public SocketCreator2() {
//		// Create the reusable sockets
//		this.binaryEcho = new MyBinaryEchoSocket();
//		this.textEcho = new MyEchoSocket();
//	}

	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
//		for (String subprotocol : req.getSubProtocols()) {
//			if ("binary".equals(subprotocol)) {
//				resp.setAcceptedSubProtocol(subprotocol);
//				return binaryEcho;
//			}
//			if ("text".equals(subprotocol)) {
//				resp.setAcceptedSubProtocol(subprotocol);
//				return textEcho;
//			}
//		}
//
//		// No valid subprotocol in request, ignore the request
//		return null;
		return new ChannelSocket();
	}
}