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
    private GameThread gameThread;
    private List<StompSession.Subscription> subscriptions ;


    public GameStompSessionHandler(String gameRoomId, String playerId, GameThread gameThread) {
        this.gameRoomId = gameRoomId;
        this.playerId = playerId;
        this.isEnd = false;
        this.gameThread = gameThread;
        this.subscriptions = new ArrayList<>();
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("stomp 연결 완료: {}", session.isConnected());

        this.session = session;

        subscriptions.add(
                session.subscribe("/topic/game/room/"+gameRoomId, new GameStartHandler())
        );
        subscriptions.add(
                session.subscribe("/topic/game/throw/"+gameRoomId, new YutThrowResultHandler())
        );
        subscriptions.add(
                session.subscribe("/topic/game/"+gameRoomId+"/mal", new MalsNextPositionHandler())
        );
        subscriptions.add(
                session.subscribe("/topic/game/"+gameRoomId+"/mal/move", new MoveMalHandler())
        );
        subscriptions.add(
                session.subscribe("/topic/game/"+gameRoomId+"/end", new GameOverHandler())
        );

        enterGame();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        throw new ConnectionException("SpringStompSessionHandler.handleException", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        // 이부분 서버 꺼져서 처음에 못붙거나, 붙었다가 서버 꺼지면 나옴 이때 재 커넥션 와일 돌리면 될꺼같음
        throw new ConnectionException("SpringStompSessionHandler.handleTransportError", exception);
    }

    /**
     * 게임 입장
     */
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

    /**
     * 윷 던지기
     */
    public void throwYut(){
        sleepRandom();
//        log.info("[{}] 윷 던지기: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("messageType", "THROW");
        params.put("room_id", gameRoomId);
        params.put("player_id", playerId);

        session.send("/app/game/throw", params);
    }

    /**
     * 말 이동가능한 위치 조회
     * @param yutResult 윷 결과
     */
    public void getMalsNextPosition(String yutResult){
        sleepRandom();
//        log.info("[{}] 말 이동 가능한 위치 조회: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", 1L);
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);
        params.put("yut_result", yutResult);

        session.send("/app/game/mal", params);
    }

    /**
     * 말 움직이기
     * @param malId 말 식별자
     * @param yutResult 윷 결과
     */
    public void moveMal(int malId, String yutResult){
        sleepRandom();
//        log.info("[{}] 말({}) 이동하기: {}", playerId, malId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", 1L);
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);
        params.put("mal_id", malId);
        params.put("yut_result", yutResult);

        session.send("/app/game/mal/move", params);
    }

    /**
     * 점수 조회
     */
    public void getScore(){
        sleepRandom();
        log.info("[{}] 점수 조회하기: {}", playerId, gameRoomId);

        Map<String, Object> params = new HashMap<>();
        params.put("player_id", playerId);
        params.put("game_id", gameRoomId);

        session.send("/app/game/score", params);
    }
    /**
     * 게임 시작
     */
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

    /**
     * 윷 던진 결과
     */
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
                if(response.getType().equals("CATCH_MAL")){
                    // 이건 윷 던진기(/game/throw)로 오는 응답이 아님. 말 이동하기 응답으로 예외적으로 오는 응답이라 테스트에서 제외.
                    return;
                }
                else { // 다른 타입(THROW, ONE_MORE_THROW)일때는 숫자로 주는데, CATCH_MAL일땐 String으로 주기 때문도 있음.
                    yutResult = YutResultConverter.intToString(yutResult);
                }

//                log.info("[{}] 윷 던진 결과 응답: {}", playerId, yutResult);
                getMalsNextPosition(yutResult);
            }
        }
    }

    /**
     * 말 이동 위치 조회
     */
    private class MalsNextPositionHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.MalsNextPositionDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.MalsNextPositionDTO response = (GameStompResponse.MalsNextPositionDTO) payload;
            if(playerId.contains(response.getPlayerId())){
//                log.info("[{}] 말 이동 위치 조회 응답: {}", playerId, gameRoomId);

                List<GameStompResponse.MalMoveInfo> moveInfoList = response.getMalList();
                if(!moveInfoList.isEmpty()){ // 기존의 윷판에 있는 말부터 움직이기
                    moveMal(moveInfoList.get(0).getMalId(), response.getYutResult());
                }
                else { // 새로운 말 움직이기
                    if(response.getNewMalId() == -1){ // 더이상 움직일 수 있는 말이 없음. 즉 게임 끝
                        // 점수 조회.
                        getScore();
                        return;
                    }
                    moveMal(response.getNewMalId(), response.getYutResult());
                }
            }
        }
    }

    /**
     * 말 이동하기
     */
    private class MoveMalHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.MoveMalDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameStompResponse.MoveMalDTO response = (GameStompResponse.MoveMalDTO) payload;
            if(playerId.contains(response.getPlayerId())){{
//                log.info("[{}] 말 이동하기 응답: id:{}, 위치:{} {}", playerId, response.getMalId(), response.getNextPosition(), gameRoomId);
            }}
            else {
                // 이제 내차례
                throwYut();
            }
        }
    }

    /**
     * 게임 종료
     */
    private class GameOverHandler implements StompFrameHandler{
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameStompResponse.GameOverDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
//            log.info("[{}] 게임 종료-{}: {}, 승자: {}", playerId, isEnd, gameRoomId, response.getWinner());
            GameStompResponse.GameOverDTO response = (GameStompResponse.GameOverDTO) payload;
            isEnd = true;
            gameThread.notifyGameEnd();
            unsubscribe();
        }
    }

    /**
     * 모든 구독 끊음.
     */
    public synchronized void unsubscribe(){
        log.info("[{}]: 모든 구독 해제. {}", playerId, gameRoomId);
        if(subscriptions.isEmpty()){
            return;
        }

        for(StompSession.Subscription subscription : subscriptions){
            subscription.unsubscribe();
        }

        subscriptions.clear();
    }

    private void sleep(Long mills){
        try {
            Thread.sleep(mills);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 랜덤한 시간만큼 sleep
     */
    private void sleepRandom(){
        int rand = (int)(Math.random() * 4) + 1;
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
