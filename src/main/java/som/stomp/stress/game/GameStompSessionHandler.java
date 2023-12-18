package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;
import som.stomp.stress.game.dto.GameStompResponse;
import som.stomp.stress.test.dto.StompResponse;

import java.lang.reflect.Type;
import java.util.*;

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
        session.subscribe("/topic/game/room/"+gameRoomId, new GameStartHandler());
        session.subscribe("/topic/game/throw/"+gameRoomId, new YutThrowResultHandler());
        session.subscribe("/topic/game/"+gameRoomId+"/mal", new MalsNextPositionHandler());
        session.subscribe("/topic/game/"+gameRoomId+"/mal/move", new MoveMalHandler());
        session.subscribe("/topic/game/"+gameRoomId+"/end", new GameOverHandler());

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
        log.info("게임 입장: {}", gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("messageType", "WAIT");
        params.put("room_id", gameRoomId);
        params.put("sender", Objects.equals(playerId, "1P") ? "가나" : "다라");
        params.put("player_id", playerId);
        params.put("profileURL_1P", "이미지주소");

        session.send("/app/game/message", params);
    }

    public void throwYut(){
        sleepRandom();
        log.info("[{}] 윷 던지기: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("messageType", "THROW");
        params.put("room_id", gameRoomId);
        params.put("player_id", playerId);

        session.send("/app/game/throw", params);
    }

    public void getMalsNextPosition(String yutResult){
        sleepRandom();
        log.info("[{}] 말 이동 가능한 위치 조회: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", 1L);
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);
        params.put("yut_result", yutResult);

        session.send("/app/game/mal", params);
    }

    public void moveMal(int malId, String yutResult){
        sleepRandom();
        log.info("[{}] 말({}) 이동하기: {}", playerId, malId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", 1L);
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);
        params.put("mal_id", malId);
        params.put("yut_result", yutResult);

        session.send("/app/game/mal/move", params);
    }

    public void getScore(){
        sleepRandom();
        log.info("[{}] 점수 조회하기: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);

        session.send("/app/game/score", params);
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
    private class GameStartHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.GameStartDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.GameStartDTO response = (GameStompResponse.GameStartDTO) payload;
            if(playerId.contains(response.getPlayerId())){
                log.info("[{}] 게임 시작 응답: {}", playerId, response.getSender());

                // 1P 먼저 윷 던져서 게임 시작!
                if(playerId.equals("1P")){
                    throwYut();
                }
            }
        }
    }

    // 윷 던진 결과
    private class YutThrowResultHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.YutThrowResultDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.YutThrowResultDTO response = (GameStompResponse.YutThrowResultDTO) payload;
            if(playerId.contains(response.getPlayerId())){
                String yutResult = response.getYut();
                if(!response.getType().equals("CATCH_MAL")){ // 다른 타입(THROW, ONE_MORE_THROW)일때는 숫자로 주는데, CATCH_MAL일땐 String으로 줘서..
                    yutResult = YutResultConverter.intToString(yutResult);
                }

                log.info("[{}] 윷 던진 결과 응답: {}", playerId, yutResult);
                getMalsNextPosition(yutResult);
            }
        }
    }

    // 말 이동 위치 조회
    private class MalsNextPositionHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.MalsNextPositionDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.MalsNextPositionDTO response = (GameStompResponse.MalsNextPositionDTO) payload;
            if(playerId.contains(response.getPlayerId())){
                log.info("[{}] 말 이동 위치 조회 응답: {}", playerId, gameRoomId);

                List<GameStompResponse.MalMoveInfo> moveInfoList = response.getMalList();
                if(!moveInfoList.isEmpty()){ // 기존의 윷판에 있는 말부터 움직이기
                    moveMal(moveInfoList.get(0).getMalId(), response.getYutResult());
                }
                else { // 새로운 말 움직이기
                    if(response.getNewMalId() == -1){
                        // 더이상 움직일 수 있는 말이 없음. 즉 게임 끝
                        // 점수 조회.
                        getScore();
                        return;
                    }
                    moveMal(response.getNewMalId(), response.getYutResult());
                }
            }
        }
    }

    // 말 이동하기
    private class MoveMalHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.MoveMalDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.MoveMalDTO response = (GameStompResponse.MoveMalDTO) payload;
            if(playerId.contains(response.getPlayerId())){{
                log.info("[{}] 말 이동하기 응답: id:{}, 위치:{}", playerId, response.getMalId(), response.getNextPosition());
            }}
            else {
                // 이제 내차례
                throwYut();
            }
        }
    }



    // 게임 종료
    private class GameOverHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.GameOverDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.GameOverDTO response = (GameStompResponse.GameOverDTO) payload;
            isEnd = true;
            log.info("[{}] 게임 종료-{}: {}, 승자: {}", playerId, isEnd, gameRoomId, response.getWinner());
        }
    }

    private void sleep(Long mills){
        try {
            Thread.sleep(mills);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sleepRandom(){
        int rand = (int)(Math.random() * 2) + 1;
        try {
            Thread.sleep(rand * 1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Boolean isEnd() {
        return this.isEnd;
    }
}
