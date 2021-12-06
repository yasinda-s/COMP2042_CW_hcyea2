package brickdestroy.levels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import brickdestroy.ball.Ball;
import brickdestroy.ball.RubberBall;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class IncreaseSpeedDropTest {

    private static IncreaseSpeedDrop increaseSpeedDrop;
    private static Ball ball1;
    private static Ball ball2;
    private static Ball ball3;
    private static Ball ball4;

    @BeforeAll
    static void setup(){
        increaseSpeedDrop = new IncreaseSpeedDrop();
        Point ballPos = new Point(410,145);

        ball1 = new RubberBall(ballPos);
        ball2 = new RubberBall(new Point(410, 170));
        ball3 = new RubberBall(new Point(330, 415));
        ball4 = new RubberBall(new Point(330, 440));
    }

    @Test
    void showBox() {
    }

    @Test
    void impact() {
        assertTrue(increaseSpeedDrop.impact(ball1)); //test when ball hits from above
        assertTrue(increaseSpeedDrop.impact(ball2)); //test when ball hits from below
        assertFalse(increaseSpeedDrop.impact(ball3)); //test when ball not touching boxface
        assertFalse(increaseSpeedDrop.impact(ball4)); //test when ball not touching boxface
    }

    @Test
    void isImpactOnce() {
        assertFalse(increaseSpeedDrop.isImpactOnce());
    }

    @Test
    void setImpactOnce() {
    }
}