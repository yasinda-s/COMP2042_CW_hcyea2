package brickdestroy.player;

import brickdestroy.ball.Ball;

import java.awt.*;

public interface PlayerInterface { //XXXX -> can remove since small player not used
    Rectangle makeRectangle(int width, int height);
    boolean impact(Ball b);
    void move();
    void moveLeft();
    void movRight();
    void stop();
    Shape getPlayerFace();
    void moveTo(Point p);
}
