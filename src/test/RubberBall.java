package test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * This is the class for Rubber Ball which inherits from Ball.
 */

public class RubberBall extends Ball {

    //properties of rubber ball shown below - the default ball type
    private static final int DEF_RADIUS = 10;
    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    /**
     * Super constructor that represents the Rubber ball.
     * @param center Takes in the center coordinates of the rubber ball.
     */
    public RubberBall(Point2D center){
        super(center,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * Used to make the shape of the Rubber Ball.
     * @param center Point2D type, consists of the x, y coordinate of the center of the ball.
     * @param radius Int type, radius of ball.
     * @return Shape object of a circle which represents the face of a circle.
     */
    @Override
    protected Shape makeBall(Point2D center, int radius) {

        double x = center.getX() - (radius / 2); //x coordinate of top left circle (rectangle)
        double y = center.getY() - (radius / 2); //y coordinate of top left circle (rectangle)

        return new Ellipse2D.Double(x,y,radius, radius);
    }
}