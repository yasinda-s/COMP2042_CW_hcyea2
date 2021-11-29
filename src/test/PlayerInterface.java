package test;

import java.awt.*;

public interface PlayerInterface {
    Rectangle makeRectangle(int width, int height);
    boolean impact(Ball b);
    void move();
    void moveLeft();
    void movRight();
    void stop();
    Shape getPlayerFace();
    void moveTo(Point p);
}
