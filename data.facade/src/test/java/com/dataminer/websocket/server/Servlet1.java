package com.dataminer.websocket.server;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/echo" })
public class Servlet1 extends WebSocketServlet {
	@Override
	public void configure(WebSocketServletFactory factory) {
		// set a 10 second timeout
		factory.getPolicy().setIdleTimeout(10000);

		// register the WebSocket to create on Upgrade
		factory.register(ChannelSocket.class);
		
		// set a custom WebSocket creator for complex socket settings.
        //factory.setCreator(new SocketCreator2());
	}
}