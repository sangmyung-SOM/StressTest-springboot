package som.stomp.stress.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

public class GameStompRequest {


    public static class ThrowYutDTO{
        @JsonProperty("messageType")
        private String type;
        @JsonProperty("room_id")
        private String roomId;
        @JsonProperty("player_id")
        private String playerId;
        @JsonProperty("yut")
        private String yut; // null로 설정

        @Builder
        public ThrowYutDTO(String type, String roomId, String playerId) {
            this.type = type;
            this.roomId = roomId;
            this.playerId = playerId;
            this.yut = null;
        }
    }

    public static class GetMalsNextPositionDTO{
        @JsonProperty("user_id")
        private Long userId;
        @JsonProperty("player_id")
        private String playerId;
        @JsonProperty("game_id")
        private String gameId;
        @JsonProperty("yut_result")
        private String yutResult;

        @Builder
        public GetMalsNextPositionDTO(String playerId, String gameId, String yutResult) {
            this.userId = 1L;
            this.playerId = playerId;
            this.gameId = gameId;
            this.yutResult = yutResult;
        }
    }

    public static class MoveMalDTO{
        @JsonProperty("user_id")
        private Long userId;
        @JsonProperty("player_id")
        private String playerId;
        @JsonProperty("game_id")
        private String gameId;
        @JsonProperty("mal_id")
        private String malId;
        @JsonProperty("yut_result")
        private String yutResult;

        @Builder
        public MoveMalDTO(String playerId, String gameId, String malId, String yutResult) {
            this.userId = 1L;
            this.playerId = playerId;
            this.gameId = gameId;
            this.malId = malId;
            this.yutResult = yutResult;
        }
    }
}
