package websocket.server;

import javax.servlet.annotation.WebServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "Echo WebSocket Servlet", urlPatterns = { "/advecho" })
public class MyAdvancedEchoServlet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.setCreator(new MyAdvancedEchoCreator());
	}
}