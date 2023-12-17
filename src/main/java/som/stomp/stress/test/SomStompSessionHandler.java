package som.stomp.stress.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;
import som.stomp.stress.dto.StompResponse;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SomStompSessionHandler extends StompSessionHandlerAdapter {

//    https://velog.io/@limsubin/STOMP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-Spring-Websocket-Client-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90

    private StompSession session;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // ...
        log.info("stomp 연결 완료: {}", session.isConnected());

        this.session = session;

        session.subscribe("/topic/test/", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return StompResponse.TestDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                StompResponse.TestDTO respone = (StompResponse.TestDTO) payload;
                log.info("응답 받음2: {}", respone.getMsg());
            }
        });

        test();
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
}
