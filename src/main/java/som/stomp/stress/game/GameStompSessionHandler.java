package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;
import som.stomp.stress.game.dto.GameStompResponse;
import som.stomp.stress.test.dto.StompResponse;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GameStompSessionHandler extends StompSessionHandlerAdapter {

//    https://velog.io/@limsubin/STOMP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-Spring-Websocket-Client-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90

    private StompSession session;
    private String gameRoomId;
    private String playerId;
    private Boolean isEnd;

    public GameStompSessionHandler(String gameRoomId, String playerId) {
        this.gameRoomId = gameRoomId;
        this.playerId = playerId;
        this.isEnd = false;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // ...
        log.info("stomp 연결 완료: {}", session.isConnected());

        this.session = session;

//        session.subscribe("/topic/test/", new TestStompConnect());
        session.subscribe("/topic/game/room/"+gameRoomId, new GameStartResponse());

//        test();
        enterGame();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("SpringStompSessionHandler.handleException");
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        // 이부분 서버 꺼져서 처음에 못붙거나, 붙었다가 서버 꺼지면 나옴 이때 재 커넥션 와일 돌리면 될꺼같음
        System.out.println("SpringStompSessionHandler.handleTransportError");
        exception.printStackTrace();
    }

    public void test(){
        Date date = new Date();
        Map<String, Object> params = new HashMap<>();
        params.put("msg", "가나 테스트" + date.toString());
        session.send("/app/test", params);
    }

    public void enterGame(){
        log.info("게임 스톰프 핸들러에서: {}", gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("messageType", "WAIT");
        params.put("room_id", gameRoomId);
        params.put("sender", playerId == "1P" ? "가나" : "다라");
        params.put("player_id", playerId);
        params.put("profileURL_1P", "이미지주소");

        session.send("/app/game/message", params);
    }

    private class TestStompConnect implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return StompResponse.TestDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            StompResponse.TestDTO respone = (StompResponse.TestDTO) payload;
            log.info("응답 받음2: {}", respone.getMsg());
        }
    }

    // 게임 시작
    private class GameStartResponse implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.GameStartDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.GameStartDTO response = (GameStompResponse.GameStartDTO) payload;
            if(playerId.contains(response.getPlayerId())){
                isEnd = true;
                log.info("게임 시작 응답: {}", response.getSender());
            }
        }
    }

    private class

    public Boolean isEnd() {
        return isEnd;
    }
}
