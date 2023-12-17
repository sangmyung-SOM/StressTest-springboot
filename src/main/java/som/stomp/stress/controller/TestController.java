package som.stomp.stress.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import som.stomp.stress.test.StompTest;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    public void test(){
        StompTest stompTest = new StompTest();

        stompTest.go();


    }
}
