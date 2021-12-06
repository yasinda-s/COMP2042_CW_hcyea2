package brickdestroy.drawcomponents;

import brickdestroy.ball.Ball;
import brickdestroy.ball.BigBall;
import brickdestroy.brick.Brick;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * DrawBall class which implements the Drawable interface. This is used to override the draw(Graphics g2d) method to draw the Ball used in game.
 */
public class DrawBall implements Drawable {
    private Ball ball;

    /**
     * Constructor of DrawBall class.
     * @param ball Ball object that will be assigned to ball.
     */
    public DrawBall(Ball ball, int level, Point2D center) {
        if(level==5){
            this.ball = new BigBall(center);
        }else{
            this.ball = ball;
        }
    }

    /**
     * Overridden method in which the ball will be drawn in the g2d Frame.
     * @param g2d Graphics 2d Frame where the ball will be drawn.
     */
    @Override
    public void draw(Graphics2D g2d, int level) { //XXXX
            Color tmp = g2d.getColor(); //set tmp color to color in g2d

            Shape s = ball.getBallFace(); //set face of ball as shape

            g2d.setColor(ball.getInnerColor()); //set color of g2d as ball color (inner)
            g2d.fill(s); //use this color and fill shape of ball face

            g2d.setColor(ball.getBorderColor()); //set outline color based on what we assigned before
            g2d.draw(s); //draw only does the outline of a shape

            g2d.setColor(tmp); //set to tmp color
        }

    @Override
    public void draw(Brick brick, Graphics2D g2d) {
    }

}
