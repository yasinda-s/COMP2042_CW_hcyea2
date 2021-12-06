package brickdestroy.brick;

import java.awt.*;
import java.util.Random;

/**
 * Color brick extends the Brick class directly. This represents the bricks that change color for every impact with the ball.
 */
public class ColorBrick extends Brick { //make interface and change how the brick only breaks from below or above impact
    Random rand = new Random();
    private static final String NAME = "Color Brick";
    private static final Color DEF_INNER = new Color(0, 0, 0);
    private static final Color DEF_BORDER = Color.WHITE;
    private static final int COLOR_STRENGTH = 2; //breaks in one impact with the ball

    public ColorBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,COLOR_STRENGTH);
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

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
