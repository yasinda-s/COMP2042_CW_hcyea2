package test.brick;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ball.Ball;
import test.ball.RubberBall;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CementBrickTest {

    private static CementBrick cementBrick;
    private static Ball ball1;
    private static Ball ball2;
    private static Ball ball3;
    private static Ball ball4;

    @BeforeAll
    static void setup(){
        cementBrick = new CementBrick(new Point(300, 420), new Dimension(60, 20));
        Point ballPos = new Point(295,425);

        ball1 = new RubberBall(ballPos);
        ball2 = new RubberBall(new Point(360, 425));
        ball3 = new RubberBall(new Point(330, 415));
        ball4 = new RubberBall(new Point(330, 440));
    }

    @Test
    void setImpact() {
    }

    @Test
    void getBorderColor() {
    }

    @Test
    void getInnerColor() {
    }

    @Test
    void findImpact() {
        assertEquals(300, cementBrick.findImpact(ball1)); //test if LEFT_IMPACT works
        assertEquals(400, cementBrick.findImpact(ball2)); //test if RIGHT_IMPACT works
        assertEquals(100, cementBrick.findImpact(ball3)); //test if UP_IMPACT works
        assertEquals(200, cementBrick.findImpact(ball4)); //test if DOWN_IMPACT works
    }

    @Test
    void isBroken() { //cement brick should not break in one impact - causes cracks only
        cementBrick.impact();
        assertFalse(cementBrick.isBroken());
    }

    @Test
    void repair() {
    }

    @Test
    void impact() {
        assertFalse(cementBrick.isBroken());
    }

    @Test
    void getBrickFace() {
    }

    @Test
    void makeBrickFace() {
    }

    @Test
    void testSetImpact() {
    }

    @Test
    void getBrick() {
    }

    @Test
    void testRepair() { //test if brickFace becomes refreshed.
    }
}