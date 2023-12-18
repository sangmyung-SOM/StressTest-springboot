package som.stomp.stress.game;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import som.stomp.stress.test.TestStompSessionHandler;

public class GameStompConnect {
    private final WebSocketStompClient stompClient;
    private final StompSessionHandler stompSessionHandler;
    private String gameRoomId;
    private String playerId;


    public GameStompConnect(String gameRoomId, String playerId){
        this.gameRoomId = gameRoomId;
        this.playerId = playerId;

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(webSocketClient);
        this.stompSessionHandler =  new GameStompSessionHandler(gameRoomId, playerId);
    }

    public void start(){
        Object[] urlVariables = {};

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect(GameConst.urlStomp, null, stompSessionHandler, urlVariables);
    }

    public Boolean isEnd(){
        GameStompSessionHandler handler =  (GameStompSessionHandler) stompSessionHandler;
        return handler.isEnd();
    }
}
