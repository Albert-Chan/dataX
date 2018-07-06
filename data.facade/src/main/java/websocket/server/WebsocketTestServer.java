package websocket.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class WebsocketTestServer {
	public static void main(String args[]) {
		Server server = new Server(7777);

		ContextHandler context = new ContextHandler();
		context.setContextPath("/test");
		
		WebSocketHandlerTest test = new WebSocketHandlerTest();
		context.setHandler(test);

		server.setHandler(context);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
