package brickdestroy.ball;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    Point ballPos = new Point(300,430);
    Ball ball = new RubberBall(ballPos);

    Color DEF_INNER_COLOR = new Color(255, 219, 88);
    Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();


    @org.junit.jupiter.api.Test
    void setInner() {
        ball.setInner(Color.ORANGE);
        assertEquals(Color.ORANGE, ball.getInnerColor());
    }

    @org.junit.jupiter.api.Test
    void move() {
    }

    @org.junit.jupiter.api.Test
    void moveTo() { //test to see if the ball actually spawns at ballpos
        ball.moveTo(ballPos);
        assertEquals(ballPos, ball.getPosition());
    }

    @org.junit.jupiter.api.Test
    void setSpeed() {
        ball.setSpeed(5, -5);
        assertTrue(ball.getSpeedX()==5 && ball.getSpeedY()==-5);
    }

    @org.junit.jupiter.api.Test
    void setXSpeed() { //test to see if the right X speed is getting set
        ball.setXSpeed(5);
        assertEquals(5, ball.getSpeedX());
    }

    @org.junit.jupiter.api.Test
    void setYSpeed() { //test to see if the right Y speed is getting set
        ball.setYSpeed(-5);
        assertEquals(-5, ball.getSpeedY());
    }

    @org.junit.jupiter.api.Test
    void reverseX() {
        ball.setXSpeed(5);
        ball.reverseX();
        assertEquals(ball.getSpeedX(), -5);
    }

    @org.junit.jupiter.api.Test
    void reverseY() {
        ball.setYSpeed(-5);
        ball.reverseY();
        assertEquals(ball.getSpeedY(), 5);
    }

    @org.junit.jupiter.api.Test
    void getBorderColor() {
        assertEquals(ball.getBorderColor(), DEF_BORDER_COLOR);
    }

    @org.junit.jupiter.api.Test
    void getInnerColor() {
        assertEquals(ball.getInnerColor(), DEF_INNER_COLOR);
    }

    @org.junit.jupiter.api.Test
    void getPosition() {
        assertEquals(ballPos, ball.getPosition());
    }

    @org.junit.jupiter.api.Test
    void getBallFace() {
    }

    @org.junit.jupiter.api.Test
    void getSpeedX() {
        ball.setXSpeed(5);
        assertEquals(ball.getSpeedX(), 5);
    }

    @org.junit.jupiter.api.Test
    void getSpeedY() {
        ball.setYSpeed(-10);
        assertEquals(ball.getSpeedY(), -10);
    }

    @org.junit.jupiter.api.Test
    void getCenter() {
        assertEquals(ball.getCenter(), ballPos);
    }

    @org.junit.jupiter.api.Test
    void getUp() {
    }

    @org.junit.jupiter.api.Test
    void getDown() {
    }

    @org.junit.jupiter.api.Test
    void getLeft() {
    }

    @org.junit.jupiter.api.Test
    void getRight() {
    }
}