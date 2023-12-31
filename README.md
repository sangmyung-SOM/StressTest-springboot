# STOMP-CLIENT 스트레스 테스트
소중한 멤버쉽 2기에서 진행한 서로알기 윷놀이 프로젝트의 스트레스 테스트용입니다.  
  
  [참고 링크]  
- https://docs.spring.io/spring-framework/reference/web/websocket/stomp/client.html  
- https://velog.io/@limsubin/STOMP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-Spring-Websocket-Client-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90

## 기본 세팅
- 자바 17 사용
- 스프링부트 버전 3 사용
- 8090 포트 사용


## 주요하게 봐야될 코드
game 패키지에 있는 클래스들이 핵심입니다. 나머지는 대부분 테스트용이니 필요하시면 보시면 됩니다.


## 실행하는 법
스프링부트 실행시킨 후  
TestController에 있는 API 요청을 확인후 필요에 맞게 호출하면 됩니다.
  
주요 메소드는  
1. [GET] /test/game/limit?cnt=  
    요청한 횟수만큼 게임 실행
2. [GET] /test/game/limit/time?cnt=&t=  
   특정 개수만큼 게임 실행, 게임 한개 생성하는데 time만큼의 딜레이 주기  
3. [GET] /test/game/count  
   완료한 게임 개수/생성한 게임 개수 = 성공률을 리턴합니다.

입니다.

만약 스프링부트를 실행시키지 않고, 게임 1개에 대해서만 테스트하려는 상황이면  
test/game 폴더의 GameTests 클래스에 있는 runOneGame() 메소드 실행시켜도 가능합니다.
