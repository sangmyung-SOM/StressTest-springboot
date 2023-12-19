package som.stomp.stress.test;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class TestStompConnect {

    public void go(){
        String url = "ws://localhost:8080/ws";

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        StompSessionHandler stompSessionHandler =  new TestStompSessionHandler();
        Object[] urlVariables = {};

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect(url, null, stompSessionHandler, urlVariables);
    }
}
