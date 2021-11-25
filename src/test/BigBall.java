package test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class BigBall extends Ball{

    private static final int DEF_RADIUS = 40;
    private static final Color DEF_INNER_COLOR = new Color(255, 219, 88);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    public BigBall(Point2D center) {
        super(center, DEF_RADIUS, DEF_INNER_COLOR, DEF_BORDER_COLOR);
    }

    @Override
    public Shape makeBall(Point2D center, int radius) {
        double x = center.getX() - ((double) radius / 2); //x coordinate of top left circle (rectangle)
        double y = center.getY() - ((double) radius / 2); //y coordinate of top left circle (rectangle)

        return new Ellipse2D.Double(x,y,radius, radius);
    }
}
