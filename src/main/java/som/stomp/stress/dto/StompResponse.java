package som.stomp.stress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StompResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TestDTO{
        private String msg;
    }
}
