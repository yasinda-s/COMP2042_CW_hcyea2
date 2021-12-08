package brickdestroy.drawcomponents;

import brickdestroy.player.Player;
import brickdestroy.ball.Ball;

import java.awt.geom.Point2D;

/**
 * Factory method design pattern used to create an in-game object and to draw it onto the screen.
 */
public class DrawFactory {
    /**
     * This method is used to draw the ball if the parameter passed is Ball object, level and center point of ball.
     * @param ball ball object.
     * @param level the level being played.
     * @param center The center of the ball's coordinates.
     * @return returns a new DrawBall(ball) object which implements the Drawable interface to create and draw the ball.
     */
    public Drawable getDraw(Ball ball, int level, Point2D center){
        return new DrawBall(ball, level, center);
    }

    /**
     * This method is used to draw the player bar if the parameter passed is Player object and level.
     * @param player Player object.
     * @param level The level being played currently.
     * @return returns a new DrawPlayer(player) object which implements the Drawable interface to create and draw the player bar.
     */
    public Drawable getDraw(Player player, int level){
        return new DrawPlayer(player,level);
    }

    /**
     * This method is used to draw the brick if no parameter is passed.
     * @return returns a new DrawBrick() object which implements the Drawable interface to draw a singular brick.
     */
    public Drawable getDraw(){
        return new DrawBrick();
    }
}

