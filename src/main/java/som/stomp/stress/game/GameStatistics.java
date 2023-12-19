package som.stomp.stress.game;

/**
 *
 */
public class GameStatistics {

    // 끝난 게임의 개수
    private int gameOverNum = 0;

    // 생성된 게임의 개수
    private int createdGameNum = 0;

    public synchronized void gameOver(){
        gameOverNum++;
    }

    public synchronized void createGame(){
        createdGameNum++;
    }

    public void reset(){
        gameOverNum = 0;
        createdGameNum = 0;
    }

    public int getGameOverNum() {
        return gameOverNum;
    }

    public int getCreatedGameNum() {
        return createdGameNum;
    }

    public String toString(){
        return "" + gameOverNum + "/" + createdGameNum;
    }
}
