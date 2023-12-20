package som.stomp.stress.game;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@NoArgsConstructor
public class GameThread extends Thread{

    private GameStatistics gameStatistics;
    private GameStompConnect player1;
    private GameStompConnect player2;

    public GameThread(GameStatistics gameStatistics){
        this.gameStatistics = gameStatistics;
    }

    @Override
    public synchronized void run() {
        super.run();

        GameApiConnect gameApiConnect = new GameApiConnect();

        // 게임 생성
        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.updateGameRoomStatus(true);
        if(gameStatistics != null){
            gameStatistics.createGame();
        }

//        try{
//            Thread.sleep(Duration.ofMinutes(5).toMillis());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

        // 플레이어 2명 생성 & 게임 시작
        try{
            player1 = new GameStompConnect(gameApiConnect.getGameRoomId(), "1P", this);
            player1.start();
            player2 = new GameStompConnect(gameApiConnect.getGameRoomId(), "2P", this);
            player2.start();
        } catch (ConnectionException e){
            // 게임 연결 실패시 게임 삭제
            gameApiConnect.deleteGame();
        } catch (Exception e){
            e.printStackTrace();
            gameApiConnect.deleteGame();
        }

        while (true){
            if(!player1.isEnd() || !player2.isEnd()){
                try {
                    log.info("아직 게임이 끝나지 않음. 1P: {}, 2P: {}", player1.isEnd(), player2.isEnd());
                    wait();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }

        // 게임 끝 & 게임 삭제
        log.info("[게임끝] 1P: {}, 2P: {}", player1.isEnd(), player2.isEnd());
        player1.unsubscribe();
        player2.unsubscribe();
        if(gameStatistics != null){
            gameStatistics.gameOver();
        }
        gameApiConnect.deleteGame();

        log.info("thread end");
    }

    /**
     * 게임이 끝났음을 알려주기
     */
    public synchronized void notifyGameEnd(){
        notify();
    }
}
