package brickdestroy.ball;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * BallInterface is an interface used to make the creation of different types of balls more maintainable. Having this enables us to decide which methods
 * are to be implemented by the children of the Ball class.
 */
public interface BallInterface {
    /**
     * Unimplemented method to make the Shape of the ball (Circle).
     * @param center Point2D type, consists of the x, y coordinate of the center of the ball.
     * @param radius Int type, radius of ball.
     * @return Returns type Shape of of a ball.
     */
    Shape makeBall(Point2D center, int radius);

    void reverseY();

    void move();

    void reverseX();

    Point2D getPosition();

    Shape getBallFace();

    Color getBorderColor();

    Color getInnerColor();
}
