package brickdestroy.drawcomponents;

import brickdestroy.brick.Brick;

import java.awt.*;

/**
 * DrawBrick class which implements the Drawable interface. This is used to override the draw(Brick brick, Graphics g2d) method to draw the Brick used in game.
 */
public class DrawBrick implements Drawable {

    @Override
    public void draw(Graphics2D g2d, int level) {
    }

    /**
     * Overridden method in which the brick will be drawn in the g2d Frame.
     * @param g2d Graphics 2d Frame where the brick will be drawn.
     */
    @Override
    public void draw(Brick brick, Graphics2D g2d) {
        //draw and color the brick
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor()); //set color based on what we assigned before
        g2d.fill(brick.getBrick());  //fill the brick based on color we set

        g2d.setColor(brick.getBorderColor()); //set outline color based on what we assigned before
        g2d.draw(brick.getBrick()); //outline the brick based on color we set

        g2d.setColor(tmp);
    }
}
