package com.dataminer.websocket.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class WebSocketServer {
	public static void main(String args[]) {

		ContextHandler context = new ContextHandler();
		context.setContextPath("/test");
		context.setHandler(new ChannelHandler());

		Server server = new Server(7777);
		server.setHandler(context);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
