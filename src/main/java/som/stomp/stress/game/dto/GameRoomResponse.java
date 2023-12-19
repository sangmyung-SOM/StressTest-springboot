package som.stomp.stress.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GameRoomResponse {

    /**
     * 게임방 생성하기
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateGameRoomDTO{
        private String roomId;
        private String roomName;
        private String category;
        private String adult;
        private Boolean playing;
    }
}
