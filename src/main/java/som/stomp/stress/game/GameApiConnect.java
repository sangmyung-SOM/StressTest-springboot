package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import som.stomp.stress.game.dto.GameRoomResponse;

import java.util.Map;

@Slf4j
public class GameApiConnect {

    // 두명의 플레이어 필요함.

    // 게임방 만들기 필요함

    // 두명의 플레이어에 대해 각각 소켓 연결 생성

    // 게임 플레이

    private final WebClient webClient;
    private String gameRoomId;

    public String getGameRoomId() {
        return gameRoomId;
    }

    public GameApiConnect(){
        // webClient 기본 설정
        this.webClient = WebClient
                        .builder()
                        .baseUrl("http://3.37.84.188:8080")
                        .build();
    }

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

        // 결과 확인
        log.info("게임방 생성 완료: {}", response.getRoomId());
        this.gameRoomId = response.getRoomId();
        return response.getRoomId();
    }

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
