package com.dataminer.websocket.server;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class ChannelHandler extends WebSocketHandler {
	@Override
	public void configure(WebSocketServletFactory webSocketServletFactory) {
		webSocketServletFactory.getPolicy().setIdleTimeout(10L * 60L * 1000L);
		webSocketServletFactory.getPolicy().setAsyncWriteTimeout(10L * 1000L);

		webSocketServletFactory.setCreator(new WebSocketCreator() {
			@Override
			public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
				return new ChannelSocket();
			}
		});
	}
}