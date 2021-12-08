package brickdestroy.game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import brickdestroy.levels.HalvePlayerDrop;
import brickdestroy.levels.IncreaseSpeedDrop;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GamePlayTest {
    private static GamePlay gamePlay;

    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static HalvePlayerDrop halvePlayerDrop = new HalvePlayerDrop();
    private static IncreaseSpeedDrop increaseSpeedDrop = new IncreaseSpeedDrop();


    @BeforeAll
    static void setup(){
        Point ballPos = new Point(300,430);
        gamePlay = new GamePlay(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2, increaseSpeedDrop, halvePlayerDrop);
        gamePlay.makeComponents(ballPos);
        gamePlay.resetLevelScores();
    }

    @Test
    void makeComponents() {
    }

    @Test
    void move() {
    }

    @Test
    void findImpacts() {
    }

    @Test
    void calculateScore() {
        gamePlay.calculateScore(5);
        assertEquals(500, gamePlay.getScore());
        assertEquals(125, gamePlay.getScoreLvlFive());
    }

    @Test
    void getBrickCount() {
    }

    @Test
    void getBallCount() {
    }

    @Test
    void isBallLost() {
    }

    @Test
    void ballReset() {
        gamePlay.ballReset();
        assertFalse(gamePlay.isBallLost());
    }

    @Test
    void wallReset() { //set one for now to make testing game easier
        assertEquals(3, gamePlay.getBallCount());
    }

    @Test
    void playerReset() {
    }

    @Test
    void ballEnd() {
    }

    @Test
    void isDone() {
    }

    @Test
    void nextLevel() {
    }

    @Test
    void hasLevel() {
    }

    @Test
    void setBallXSpeed() {
    }

    @Test
    void setBallYSpeed() {
    }

    @Test
    void resetBallCount() {
    }

    @Test
    void incrementTime() {
    }

    @Test
    void getLevelTimePlayed() {
    }

    @Test
    void setLevelTimePlayed() {
    }

    @Test
    void getScore() {
    }

    @Test
    void setScore() {
    }

    @Test
    void resetLevelScores() {
        gamePlay.resetLevelScores();
        assertEquals(0, gamePlay.getScoreLvlFive());
    }

    @Test
    void skipLevel() {
    }

    @Test
    void getLevel() {
    }

    @Test
    void getScoreLvlOne() {
    }

    @Test
    void getScoreLvlTwo() {
    }

    @Test
    void getScoreLvlThree() {
    }

    @Test
    void getScoreLvlFour() {
    }

    @Test
    void getScoreLvlFive() {
    }

    @Test
    void getScoreLvlSix() {
    }

    @Test
    void getBall() {
    }
}