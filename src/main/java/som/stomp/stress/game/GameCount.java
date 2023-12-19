package som.stomp.stress.game;

public class GameCount {

    int gameCount = 0;

    public synchronized void gameOver(){
        gameCount++;
    }

    public void reset(){
        gameCount = 0;
    }

    public int getGameCount() {
        return gameCount;
    }
}
