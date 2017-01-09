package websocket.websocket.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SystemInformationHandler extends TextWebSocketHandler {
	Log logger = LogFactory.getLog(SystemInformationHandler.class);
	Set<WebSocketSession> peers = null;
	
	private static SystemInformationHandler instance = null;
	private SystemInformationHandler(){
		peers = Collections.synchronizedSet(new HashSet<WebSocketSession>());
	}
	
	public static SystemInformationHandler getInstance(){
		if(instance == null){
			instance = new SystemInformationHandler();
		}
		return instance;
	}
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		peers.add(session);
		TextMessage message = new TextMessage("Hello Message");
		session.sendMessage(message);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.debug("Received: " + message);
		session.close();
	}
	
	public void postMessage(String message) throws IOException{
		for (WebSocketSession webSocketSession : peers) {
			webSocketSession.sendMessage(new TextMessage(message));
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		peers.remove(session);
	}
}
