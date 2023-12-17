package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameThread extends Thread{

    @Override
    public void run() {
        super.run();

        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");

        GameStompConnect gameStompConnect1 = new GameStompConnect(gameApiConnect.getGameRoomId(), "1P");
        gameStompConnect1.start();
        GameStompConnect gameStompConnect2 = new GameStompConnect(gameApiConnect.getGameRoomId(), "2P");
        gameStompConnect2.start();

        while (!gameStompConnect1.isEnd() || !gameStompConnect2.isEnd()){
            try {
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        gameApiConnect.deleteGame();

        log.info("thread end");
    }
}
