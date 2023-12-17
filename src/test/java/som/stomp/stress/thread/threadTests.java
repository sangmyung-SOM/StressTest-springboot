package som.stomp.stress.thread;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class threadTests {

    @Test
    public void 쓰레드_테스트1(){
        sleepAndPrint(1000L);
        System.out.println("쓰레드 테스트 1");
    }

    public void sleepAndPrint(Long mills){
        try {
            Thread.sleep(mills);
            System.out.println("sleep 후 출력! " + mills);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
