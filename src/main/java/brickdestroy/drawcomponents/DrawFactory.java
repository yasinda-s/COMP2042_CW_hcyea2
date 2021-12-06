package brickdestroy.drawcomponents;

import brickdestroy.player.Player;
import brickdestroy.ball.Ball;

import java.awt.geom.Point2D;

/**
 * Factory method design used to generate an in-game object to draw onto the screen.
 */
public class DrawFactory {
    /**
     * This method is used to draw the ball if the parameter passed is Ball object.
     * @param ball Ball object
     * @return returns a new DrawBall(ball) object which implements the Drawable interface to draw the ball.
     */
    public Drawable getDraw(Ball ball, int level, Point2D center){
        return new DrawBall(ball, level, center);
    }

    /**
     * This method is used to draw the player bar if the parameter passed is Player object.
     * @param player Player object.
     * @param level The level being played currently.
     * @return returns a new DrawPlayer(player) object which implements the Drawable interface to draw the player bar.
     */
    public Drawable getDraw(Player player, int level){
        return new DrawPlayer(player,level);
    }

    /**
     * This method is used to draw the brick.
     * @return returns a new DrawBall() object which implements the Drawable interface to draw a singular brick.
     */
    public Drawable getDraw(){
        return new DrawBrick();
    }


}

