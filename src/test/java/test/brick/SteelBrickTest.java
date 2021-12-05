package test.brick;

import org.junit.jupiter.api.Test;
import test.ball.Ball;
import test.ball.RubberBall;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SteelBrickTest {
    SteelBrick steelBrick = new SteelBrick(new Point(300, 420), new Dimension(60, 20));
    Point ballPos = new Point(295,425);

    Ball ball1 = new RubberBall(ballPos);
    Ball ball2 = new RubberBall(new Point(360, 425));
    Ball ball3 = new RubberBall(new Point(330, 415));
    Ball ball4 = new RubberBall(new Point(330, 440));

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
        assertEquals(300, steelBrick.findImpact(ball1)); //test if LEFT_IMPACT works
        assertEquals(400, steelBrick.findImpact(ball2)); //test if RIGHT_IMPACT works
        assertEquals(100, steelBrick.findImpact(ball3)); //test if UP_IMPACT works
        assertEquals(200, steelBrick.findImpact(ball4)); //test if DOWN_IMPACT works
    }

    @Test
    void isBroken() {
    }

    @Test
    void repair() {
    }

    @Test
    void impact() {
    }

    @Test
    void getBrickFace() {
    }

    @Test
    void makeBrickFace() {
    }

    @Test
    void getBrick() {
    }

    @Test
    void testSetImpact() {
    }

    @Test
    void testImpact() {
    }
}