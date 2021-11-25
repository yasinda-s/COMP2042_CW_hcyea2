package test;

import java.awt.*;
import java.awt.geom.Point2D;

public interface BallInterface {
    Shape makeBall(Point2D center, int radius);

    void reverseY();

    void move();

    void reverseX();

    Point2D getPosition();

    Shape getBallFace();

    Color getBorderColor();

    Color getInnerColor();
}
