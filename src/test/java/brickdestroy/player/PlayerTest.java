package brickdestroy.player;

import org.junit.jupiter.api.Test;
import brickdestroy.ball.Ball;
import brickdestroy.ball.RubberBall;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;
    Player player = new Player(new Point(300, 420),150,10, new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT));
    private Point ballPos = new Point(350, 420);
    Ball ball = new RubberBall(ballPos);

    @Test
    void makeRectangle() {
    }

    @Test
    void impact() {
        assertTrue(player.impact(ball));
    }

    @Test
    void move() {
    }

    @Test
    void changeWidth() {
        player.changeWidth();
        assertEquals(40, player.getWidth());
    }

    @Test
    void resetWidth() {
        player.changeWidth();
        player.resetWidth();
        assertEquals(150, player.getWidth());
    }

    @Test
    void getWidth() {
    }

    @Test
    void increaseWidth() {
        player.resetWidth();
        player.increaseWidth();
        assertEquals(200, player.getWidth());
    }

    @Test
    void halveWidth() {
        player.resetWidth();
        player.halveWidth();
        assertEquals(75, player.getWidth());
    }

    @Test
    void moveLeft() {
    }

    @Test
    void movRight() {
    }

    @Test
    void stop() {
    }

    @Test
    void getPlayerFace() {
    }

    @Test
    void moveTo() {
    }
}