package websocket.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 128 * 1024, maxBinaryMessageSize = 128 * 1024)
public class AnnotatedEchoSocket {

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		System.out.println("server: @OnWebSocketConnect");
		if (session.isOpen()) {
			session.getRemote().sendString("I am the server.");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			session.getRemote().sendString(df.format(new Date()));
		}
	}

	@OnWebSocketClose
	public void onWebSocketBinary(int i, String string) {
		System.out.println("server: @OnWebSocketClose");
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("<<<<<<<<<<<<<<<<<<%s%n", msg);
	}

	public void onWebSocketBinary(org.eclipse.jetty.websocket.api.Session session, int a, java.lang.String s) {

	}

}