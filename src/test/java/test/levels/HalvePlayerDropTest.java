package test.levels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ball.Ball;
import test.ball.RubberBall;
import test.levels.HalvePlayerDrop;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class HalvePlayerDropTest {

    private static HalvePlayerDrop halvePlayerDrop;
    private static Ball ball1;
    private static Ball ball2;
    private static Ball ball3;
    private static Ball ball4;

    @BeforeAll
    static void setup(){
        halvePlayerDrop = new HalvePlayerDrop();
        Point ballPos = new Point(210,145);

        ball1 = new RubberBall(ballPos);
        ball2 = new RubberBall(new Point(210, 170));
        ball3 = new RubberBall(new Point(330, 415));
        ball4 = new RubberBall(new Point(330, 440));
    }

    @Test
    void showBox() {
    }

    @Test
    void impact() {
        assertTrue(halvePlayerDrop.impact(ball1)); //test when ball hits from above
        assertTrue(halvePlayerDrop.impact(ball2)); //test when ball hits from below
        assertFalse(halvePlayerDrop.impact(ball3)); //test when ball not touching boxface
        assertFalse(halvePlayerDrop.impact(ball4)); //test when ball not touching boxface
    }

    @Test
    void isImpactOnce() {
        assertFalse(halvePlayerDrop.isImpactOnce());
    }

    @Test
    void setImpactOnce() {
    }
}