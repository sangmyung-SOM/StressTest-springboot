package som.stomp.stress.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GameStompResponse {

    /**
     * 게임 시작 응답
     */
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class GameStartDTO{
        @JsonProperty("messageType")
        private String messageType;

        @JsonProperty("player_id") // 1p : 1, 2p : 2
        private String playerId;

        @JsonProperty("sender")
        private String sender;

        private String message;

        private String userNameList;

        private String profileURL_1P;

        private String profileURL_2;
    }

    /**
     * 윷 던진 결과 응답
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class YutThrowResultDTO{
        @JsonProperty("messageType")
        private String type;
        @JsonProperty("room_id")
        private String roomId;
        @JsonProperty("player_id")
        private String playerId;
        @JsonProperty("yut")
        private String yut; // [0, 1, 2, 3, 4, 5]
    }

    /**
     * 게임 오버 응답
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GameOverDTO{
        private String winner;
        private String loser;
    }

    /**
     * 모든 말의 이동가능한 위치조회 응답
     */
    @NoArgsConstructor
    @Getter
    public static class MalsNextPositionDTO{
        private Long userId;
        private String playerId;
        private String yutResult;
        private int newMalId;
        private List<MalMoveInfo> malList;
    }

    @NoArgsConstructor
    @Getter
    public static class MalMoveInfo{
        private int malId;
        private Boolean isEn;
        private int point;
        private int position;
        private int nextPosition;
    }

    /**
     * 말 움직이기 응답
     */
    @NoArgsConstructor
    @Getter
    public static class MoveMalDTO{
        private Long userId;
        private String playerId;
        private int malId;
        private int point;
        private List<Integer> movement;
        private int nextPosition;
        private Boolean isEnd;
        private Boolean isCatchMal;
        private List<Integer> catchMalList;
        private Boolean isUpdaMal;
        private int updaMalId;
    }
}
