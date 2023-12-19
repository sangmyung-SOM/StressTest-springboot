package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import som.stomp.stress.base.BaseData;
import som.stomp.stress.game.dto.GameRoomResponse;

import java.util.Map;

@Slf4j
public class GameApiConnect {
    private final WebClient webClient;
    private String gameRoomId;

    public String getGameRoomId() {
        return gameRoomId;
    }

    public GameApiConnect(){
        // webClient 기본 설정
        this.webClient = WebClient
                        .builder()
                        .baseUrl(BaseData.urlApi)
                        .build();
    }

    /**
     * 게임방 생성하기
     * @param playerNickname 플레이어 닉네임
     * @param gameCategory 게임내 질문 카테고리 [COUPLE, MARRIED, PARENT]
     * @param isAdult 성인 질문 포함 영부 [ON, OFF]
     * @return 게임방 식별자
     */
    public String createGame(String playerNickname, String gameCategory, String isAdult){

        StringBuilder urlStringBuilder = new StringBuilder("/game/room?")
                .append("name=")
                .append(playerNickname)
                .append("&category=")
                .append(gameCategory)
                .append("&adult=")
                .append(isAdult);

        // api 요청
        GameRoomResponse.CreateGameRoomDTO response = webClient
                        .post()
                        .uri(urlStringBuilder.toString())
                        .retrieve()
                        .bodyToMono(GameRoomResponse.CreateGameRoomDTO.class)
                        .block();

        log.info("게임방 생성 완료: {}", response.getRoomId());
        this.gameRoomId = response.getRoomId();
        return response.getRoomId();
    }

    /**
     * 게임방 삭제하기
     */
    public void deleteGame(){
        if(gameRoomId == null){
            throw new RuntimeException("게임방이 생성되어있지 않음!");
        }

        StringBuilder urlStringBuilder = new StringBuilder("/game/room/")
                .append(gameRoomId);

        ResponseEntity<Map> response = webClient
                .delete()
                .uri(urlStringBuilder.toString())
                .retrieve()
                .toEntity(Map.class)
                .block();

        log.info("게임방 삭제 응답: {}", response.getStatusCode().toString());
        this.gameRoomId = null;
    }

    /**
     * 게임방 상테 업데이트
     * @param state
     */
    public void updateGameRoomStatus(Boolean state){
        if(gameRoomId == null){
            throw new RuntimeException("게임방이 생성되어있지 않음!");
        }

        StringBuilder urlStringBuilder = new StringBuilder("/game/room/")
                .append(gameRoomId)
                .append("/update")
                .append("?state=")
                .append(state);

        // api 요청
        Boolean response = webClient
                .patch()
                .uri(urlStringBuilder.toString())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        // 결과 확인
        log.info("게임방 상태 업데이트 응답: {}", response);
    }
}
