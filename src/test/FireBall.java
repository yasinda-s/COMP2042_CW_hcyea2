package test;

import java.awt.*;
import java.awt.geom.Point2D;

public class FireBall extends Ball{

    private static final int DEF_RADIUS = 10;
    private static final Color DEF_INNER_COLOR = new Color(255, 0, 0);
    private static final Color DEF_BORDER_COLOR = new Color(83, 0, 0);

    public FireBall(Point2D center){
        super(center, DEF_RADIUS, DEF_INNER_COLOR, DEF_BORDER_COLOR);
    }

    @Override
    public Shape makeBall(Point2D center, int radius) {
        return null;
    }
}
