package websocket.server;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;




public class MyAdvancedEchoCreator  implements org.eclipse.jetty.websocket.servlet.WebSocketCreator{


    AnnotatedEchoSocket annotatedEchoSocket;

    public MyAdvancedEchoCreator() {
        annotatedEchoSocket = new AnnotatedEchoSocket();

    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        for (String sub : req.getSubProtocols())
        {
            /**
             *   官方的Demo，这里可以根据相应的参数做判断，使用什么样的websocket
             */

        }

        // 没有有效的请求，忽略它
        return annotatedEchoSocket;

    }
}

















//public class MyAdvancedEchoCreator implements WebSocketCreator {
//	private MyBinaryEchoSocket binaryEcho;
//	private MyEchoSocket textEcho;
//
//	public MyAdvancedEchoCreator() {
//		// Create the reusable sockets
//		this.binaryEcho = new MyBinaryEchoSocket();
//		this.textEcho = new MyEchoSocket();
//	}
//
//	@Override
//    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp)
//    {
//        for (String subprotocol : req.getSubProtocols())
//        {
//            if ("binary".equals(subprotocol))
//            {
//                resp.setAcceptedSubProtocol(subprotocol);
//                return binaryEcho;
//            }
//            if ("text".equals(subprotocol))
//            {
//                resp.setAcceptedSubProtocol(subprotocol);
//                return textEcho;
//            }
//        }
//
//        // No valid subprotocol in request, ignore the request
//        return null;
//    }