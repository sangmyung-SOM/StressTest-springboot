package som.stomp.stress.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import som.stomp.stress.game.GameApiConnect;
import som.stomp.stress.game.GameCount;
import som.stomp.stress.game.GameStompConnect;
import som.stomp.stress.game.GameThread;
import som.stomp.stress.test.StompTest;

@RestController
@RequestMapping("/test")
public class TestController {

    GameCount gameCount = new GameCount();

    @GetMapping("/")
    public void test(){
        StompTest stompTest = new StompTest();

        stompTest.go();
    }

    @GetMapping("/2")
    public void test2(){
        GameApiConnect gameApiConnect = new GameApiConnect();

        gameApiConnect.createGame("가나", "COUPLE", "ON");
        gameApiConnect.deleteGame();

        StompTest stompTest = new StompTest();
        stompTest.go();

        System.out.println("동기 확인");
    }

    @GetMapping("/3")
    public void test3(){
//        GameApiConnect gameApiConnect = new GameApiConnect();
//
//        gameApiConnect.createGame("가나", "COUPLE", "ON");
//
//        GameStompConnect gameStompConnect1 = new GameStompConnect(gameApiConnect.getGameRoomId(), "1P");
//        gameStompConnect1.start();
//        GameStompConnect gameStompConnect2 = new GameStompConnect(gameApiConnect.getGameRoomId(), "2P");
//        gameStompConnect2.start();
//
//        gameApiConnect.deleteGame();
//
//        System.out.println("동기 확인");
    }

    @GetMapping("/4")
    public void test4(){
        GameThread gameThread = new GameThread(gameCount);
        gameThread.start();

        System.out.println("동기 확인");
    }

    @GetMapping("/5")
    public void test5(){
        for(int i=0; i<100; i++){
            GameThread gameThread = new GameThread(gameCount);
            gameThread.start();
        }
    }

    @GetMapping("/6")
    public void test6(){
        for(int i=0; i<400; i++){
            GameThread gameThread = new GameThread(gameCount);
            gameThread.start();
        }
    }

    @GetMapping("/limit")
    public int testCount(@RequestParam("cnt") int count){
        for(int i=0; i<count; i++){
            GameThread gameThread = new GameThread(gameCount);
            gameThread.start();
        }

        return gameCount.getGameCount();
    }

    @GetMapping("/limit")
    public int testCountWithSleep(@RequestParam("cnt") int count, @RequestParam("t") Long time){
        for(int i=0; i<count; i++){
            GameThread gameThread = new GameThread(gameCount);
            gameThread.start();
            try{
                Thread.sleep(time);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return gameCount.getGameCount();
    }

    @GetMapping("/reset")
    public void resetCount(){
        gameCount.reset();
    }

    @GetMapping("/gameover")
    public int getGameOverCnt(){
        return gameCount.getGameCount();
    }
}
