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
        super(center,DEF_RADIUS,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * Used to make the shape of the Rubber Ball.
     * @param center Point2D type, consists of the x, y coordinate of the center of the ball.
     * @param radiusA Int type, radius of ball.
     * @param radiusB Int type, radius of ball.
     * @return
     */
    @Override
    protected Shape makeBall(Point2D center, int radiusA, int radiusB) {

        double x = center.getX() - (radiusA / 2); //x coordinate of top left circle (rectangle)
        double y = center.getY() - (radiusB / 2); //y coordinate of top left circle (rectangle)

        return new Ellipse2D.Double(x,y,radiusA,radiusB);
    }
}
