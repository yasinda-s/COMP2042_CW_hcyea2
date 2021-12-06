package brickdestroy.brick;

import org.junit.jupiter.api.Test;
import brickdestroy.ball.Ball;
import brickdestroy.ball.RubberBall;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ColorBrickTest {
    ColorBrick colorBrick = new ColorBrick(new Point(300, 420), new Dimension(60, 20));
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
        assertEquals(300, colorBrick.findImpact(ball1)); //test if LEFT_IMPACT works
        assertEquals(400, colorBrick.findImpact(ball2)); //test if RIGHT_IMPACT works
        assertEquals(100, colorBrick.findImpact(ball3)); //test if UP_IMPACT works
        assertEquals(200, colorBrick.findImpact(ball4)); //test if DOWN_IMPACT works
    }

    @Test
    void isBroken() { //should not break after one impact
        colorBrick.impact();
        assertFalse(colorBrick.isBroken());
    }

    @Test
    void repair() {
    }

    @Test
    void impact() { //test if brick doesnt break and if color changes after impact
        assertFalse(colorBrick.isBroken());
        assertNotSame(colorBrick.getInnerColor(), new Color(0, 0, 0));
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
    void testImpact() {
    }
}