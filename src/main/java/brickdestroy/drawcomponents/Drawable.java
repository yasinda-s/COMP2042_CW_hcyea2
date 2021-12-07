package brickdestroy.drawcomponents;

import brickdestroy.brick.Brick;
import java.awt.*;

/**
 * This interface is used to draw the different in-game objects. Other classes implements this interface to draw in-game objects based on parameters passed in.
 */
interface Drawable{
    /**
     * This draw method takes in two parameters (Graphics2d) and drawing object (Ball or Player) which will later be used to draw the Ball and Player objects.
     * @param g2d Graphics2d frame where the ball or Player will be drawn.
     */
    void draw(Graphics2D g2d, int level);

    /**
     * This draw method takes in two parameters (Brick, Graphics2d) which will later be used to draw the Brick object.
     * @param brick Brick object.
     * @param g2d Graphics2d frame where the Brick will be drawn.
     */
    void draw(Brick brick, Graphics2D g2d);
}