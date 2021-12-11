package brickdestroy.brick;

import java.awt.*;
import java.util.Random;

/**
 * Color brick extends the Brick class directly. This represents the bricks that change color for every impact with the ball.
 */
public class ColorBrick extends Brick {
    Random rand = new Random();
    private static final Color DEF_INNER = new Color(0, 0, 0);
    private static final Color DEF_BORDER = Color.WHITE;
    private static final int COLOR_STRENGTH = 2;

    /**
     * This is the constructor for the ColorBrick, it inherits most from the super constructor.
     * @param point Refers to the top left coordinates of the Color Brick.
     * @param size Refers to the dimension of the brick - width and height.
     */
    public ColorBrick(Point point, Dimension size){
        super(point,size,DEF_BORDER,DEF_INNER,COLOR_STRENGTH);
    }

    /**
     * Method to make the Color Brick's Face (Rectangle Shape)
     * @param pos The top left position (coordinates) of the brick.
     * @param size The width and height of the brick (Dimension).
     * @return Returns a rectangular Shape representing shape of a brick.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * Method to get the brick Face of Color Brick.
     * @return Returns the brickFace of the Color Brick.
     */
    @Override
    public Shape getBrick() {
        return super.getBrickFace();
    }

    /**
     * Method used to decrease the strength of a brick on impact and to change the color of the brick for each impact with the ball.
     */
    public void impact(){
        strength--;
        if(strength==1) {
            inner = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        broken = (strength == 0);
    }
}
