package som.stomp.stress.game;

import org.junit.jupiter.api.Test;
import som.stomp.stress.test.TestStompConnect;

public class GameTests {

    /**
     * stomp 연결 테스트
     */
    @Test
    public void testStompConnect(){
        TestStompConnect testStompConnect = new TestStompConnect();
        testStompConnect.go(); // 출력 안되고 끝나는 이유는 이 메소드 자체가 끝나버려서임. 이 메소드가 기다려주면 됨.
    }

    /**
     * 게임 생성, 삭제 API 테스트
     */
    @Test
    public void testGameCreateAndDelete(){
        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.deleteGame();
    }

    /**
     * 게임 한개 실행하기
     */
    @Test
    public void runGameOne(){
        GameThread gameThread = new GameThread();
        gameThread.start();

        while(gameThread.isAlive()){
            continue;
        }
    }
}
