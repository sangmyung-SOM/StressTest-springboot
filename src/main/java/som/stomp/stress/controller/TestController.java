package som.stomp.stress.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import som.stomp.stress.game.GameApiConnect;
import som.stomp.stress.game.GameStatistics;
import som.stomp.stress.game.GameThread;
import som.stomp.stress.test.TestStompConnect;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    GameStatistics gameStatistics = new GameStatistics();

    /**
     * stomp 연결 확인하기
     */
    @GetMapping("/stomp")
    public void testStompConnect(){
        TestStompConnect testStompConnect = new TestStompConnect();

        testStompConnect.go();
    }

    /**
     * API 연결 확인
     * 게임 생성 및 삭제
     */
    @GetMapping("/api")
    public void testGameCreateAndDelete(){
        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.deleteGame();
    }

    /**
     * 게임 하나 실행
     */
    @GetMapping("/game")
    public void testRunOneGame(){
        GameThread gameThread = new GameThread(gameStatistics);
        gameThread.start();
    }

    /**
     * 게임 100개 실행
     */
    @GetMapping("/game/100")
    public void testRun100Game(){
        for(int i=0; i<100; i++){
            GameThread gameThread = new GameThread(gameStatistics);
            gameThread.start();
        }
    }

    /**
     * 특정 개수만큼 게임 실행
     * @param count 게임 횟수
     * @return
     */
    @GetMapping("/game/limit")
    public void testRunGameToLimit(@RequestParam("cnt") int count){
        for(int i=0; i<count; i++){
            GameThread gameThread = new GameThread(gameStatistics);
            gameThread.start();
        }
    }

    /**
     * 특정 개수만큼 게임 실행, 게임 한개 실행하는데 time만큼의 딜레이 주기
     * @param count 게임 횟수
     * @param time 게임 실행하기 전 delay할 시간
     */
    @GetMapping("/game/limit/time")
    public void testRunGameToLimit(@RequestParam("cnt") int count, @RequestParam("t") Long time){

        for(int i=0; i<count; i++){
            GameThread gameThread = new GameThread(gameStatistics);
            gameThread.start();
            try{
                Thread.sleep(time);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 게임 완료 개수 초기화
     */
    @GetMapping("/game/count/reset")
    public void resetGameOverCount(){
        gameStatistics.reset();
    }

    /**
     * 게임 완료 개수 조회
     * @return 게임 완료 개수
     */
    @GetMapping("/game/count")
    public String getGameOverCnt(){
        return gameStatistics.toString();
    }
}
