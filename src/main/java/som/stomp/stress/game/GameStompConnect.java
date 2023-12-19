package som.stomp.stress.game;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import som.stomp.stress.base.BaseData;

public class GameStompConnect {
    private final WebSocketStompClient stompClient;
    private final StompSessionHandler stompSessionHandler;
    private final String gameRoomId;
    private final String playerId;


    public GameStompConnect(String gameRoomId, String playerId, GameThread gameThread){
        this.gameRoomId = gameRoomId;
        this.playerId = playerId;

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(webSocketClient);
        this.stompSessionHandler =  new GameStompSessionHandler(gameRoomId, playerId, gameThread);
    }

    public void start(){
        Object[] urlVariables = {};

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connectAsync(BaseData.urlStomp, null, stompSessionHandler, urlVariables);
    }

    /**
     * 게임이 끝났는지 여부
     * @return if game end, return true
     */
    public Boolean isEnd(){
        GameStompSessionHandler handler =  (GameStompSessionHandler) stompSessionHandler;
        return handler.isEnd();
    }
}
