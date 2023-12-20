package som.stomp.stress.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import som.stomp.stress.base.BaseData;
import som.stomp.stress.test.TestStompSessionHandler;

@Configuration
public class ClientWebSocketStompConfig {

    // 테스트! 실제로 이거 쓰면 한 유저에 대해서밖에 대응 못함!
//
//    @Bean
//    public WebSocketStompClient WebSocketStompClient(WebSocketStompClient webSocketClient,
//                                                     StompSessionHandler stompSessionHandler) {
//
//        // client to server message converter
//        webSocketClient.setMessageConverter(new MappingJackson2MessageConverter());
//
//
//        Object[] urlVariables = {};
//        String url = BaseData.urlStomp;
//        webSocketClient.connect(url, null, stompSessionHandler, urlVariables);
//
//        return webSocketClient;
//    }
//
//    @Bean
//    public WebSocketStompClient webSocketClient() {
//        WebSocketClient webSocketClient = new StandardWebSocketClient();
//        return new WebSocketStompClient(webSocketClient);
//    }
//    @Bean
//    public StompSessionHandler stompSessionHandler() {
//        return new TestStompSessionHandler();
//    }
}
