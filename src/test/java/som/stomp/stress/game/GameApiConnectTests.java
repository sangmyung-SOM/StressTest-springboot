package som.stomp.stress.game;

import org.junit.jupiter.api.Test;
import som.stomp.stress.test.StompTest;

public class GameApiConnectTests {

    @Test
    public void createRoom(){
        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.deleteGame();

        StompTest stompTest = new StompTest();
        stompTest.go(); // 출력 안되고 끝나는 이유는 이 메소드 자체가 끝나버려서임. 이 메소드가 기다려주면 됨.

        System.out.println("동기 확인");
    }

    @Test
    public void createRoom2(){
        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.deleteGame();

        GameStompConnect gameStompConnect = new GameStompConnect(gameApiConnect.getGameRoomId(), "1P");
        gameStompConnect.start();

        System.out.println("동기 확인");
    }

    @Test
    public void runOnThread(){
        GameThread gameThread = new GameThread();
        gameThread.start();

        while(gameThread.isAlive()){
            continue;
        }
    }
}
