package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameThread extends Thread{

    private GameCount gameCount;

    public GameThread(GameCount gameCount){
        this.gameCount = gameCount;
    }

    @Override
    public synchronized void run() {
        super.run();

        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.updateGameRoomStatus(true);

        GameStompConnect gameStompConnect1 = new GameStompConnect(gameApiConnect.getGameRoomId(), "1P", this);
        gameStompConnect1.start();
        GameStompConnect gameStompConnect2 = new GameStompConnect(gameApiConnect.getGameRoomId(), "2P", this);
        gameStompConnect2.start();

//        while (!gameStompConnect1.isEnd() || !gameStompConnect2.isEnd()){
//            try{
//                log.info("1P: {}, 2P: {}", gameStompConnect1.isEnd(), gameStompConnect2.isEnd());
//                Thread.sleep(10000);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        while (true){
            if(!gameStompConnect1.isEnd() || !gameStompConnect2.isEnd()){
                try {
                    log.info("아직 게임이 끝나지 않음. 1P: {}, 2P: {}", gameStompConnect1.isEnd(), gameStompConnect2.isEnd());
                    wait();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }

        log.info("1P: {}, 2P: {}", gameStompConnect1.isEnd(), gameStompConnect2.isEnd());
        gameApiConnect.deleteGame();
        gameCount.gameOver();

        log.info("thread end");
    }

    public synchronized void notifyEndGame(){
        notify();
    }
}
